import os
import sys
from pathlib import Path

def check_dependencies():
    required = ['pymupdf', 'python-docx', 'mammoth', 'pywin32']
    missing = []
    for pkg in required:
        try:
            if pkg == 'pymupdf':
                import fitz
            elif pkg == 'python-docx':
                import docx
            elif pkg == 'mammoth':
                import mammoth
            elif pkg == 'pywin32':
                import win32com
        except ImportError:
            missing.append(pkg)
    
    if missing:
        print(f"缺少依赖包: {', '.join(missing)}")
        print("正在安装...")
        import subprocess
        subprocess.check_call([sys.executable, '-m', 'pip', 'install', '-i', 'https://pypi.org/simple'] + missing)
        print("依赖安装完成!")

def pdf_to_md(pdf_path, output_path):
    import fitz
    doc = fitz.open(pdf_path)
    md_content = []
    
    for page_num in range(len(doc)):
        page = doc[page_num]
        text = page.get_text()
        if text.strip():
            md_content.append(f"## 第 {page_num + 1} 页\n\n{text}\n\n")
    
    doc.close()
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(''.join(md_content))
    
    return True

def doc_to_md(doc_path, output_path):
    import mammoth
    with open(doc_path, 'rb') as f:
        result = mammoth.convert_to_markdown(f)
        md_content = result.value
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(md_content)
    
    return True

def old_doc_to_md(doc_path, output_path):
    import win32com.client
    import pythoncom
    
    pythoncom.CoInitialize()
    
    word = None
    doc = None
    try:
        word = win32com.client.Dispatch("Word.Application")
        word.Visible = False
        
        doc = word.Documents.Open(str(doc_path.absolute()))
        
        text = doc.Content.Text
        
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(text)
        
        return True
    except Exception as e:
        raise Exception(f"Word转换失败: {str(e)}")
    finally:
        if doc:
            doc.Close(False)
        if word:
            word.Quit()
        pythoncom.CoUninitialize()

def convert_files():
    source_dir = Path(r'e:\CODE\algo-shiyan\tech_documents')
    output_dir = Path(r'e:\CODE\algo-shiyan\tech_documents\python')
    output_dir.mkdir(exist_ok=True)
    
    supported_extensions = {'.pdf', '.doc', '.docx'}
    
    converted = 0
    skipped = 0
    errors = []
    
    for file_path in source_dir.iterdir():
        if not file_path.is_file():
            continue
        
        if file_path.suffix.lower() not in supported_extensions:
            continue
        
        if file_path.name.startswith('~$'):
            continue
        
        output_file = output_dir / (file_path.stem + '.md')
        
        if output_file.exists():
            print(f"跳过 (已存在): {file_path.name}")
            skipped += 1
            continue
        
        print(f"转换中: {file_path.name} -> {output_file.name}")
        
        try:
            if file_path.suffix.lower() == '.pdf':
                pdf_to_md(file_path, output_file)
            elif file_path.suffix.lower() == '.docx':
                doc_to_md(file_path, output_file)
            elif file_path.suffix.lower() == '.doc':
                old_doc_to_md(file_path, output_file)
            
            print(f"✓ 完成: {file_path.name}")
            converted += 1
        except Exception as e:
            error_msg = f"✗ 失败: {file_path.name} - {str(e)}"
            print(error_msg)
            errors.append(error_msg)
    
    print("\n" + "="*50)
    print(f"转换完成!")
    print(f"成功转换: {converted} 个文件")
    print(f"跳过: {skipped} 个文件")
    if errors:
        print(f"失败: {len(errors)} 个文件")
        for err in errors:
            print(f"  - {err}")

if __name__ == '__main__':
    print("文档转换工具 (PDF/DOC -> MD)")
    print("="*50)
    
    check_dependencies()
    
    convert_files()
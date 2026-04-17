class AnimationController {
  constructor() {
    this.isAnimating = false;
    this.currentStep = 0;
    this.animationTimer = null;
    this.animationSteps = [];
    this.init();
  }

  init() {
    this.bindEvents();
  }

  bindEvents() {
    const speedSlider = document.getElementById('speedSlider');
    if (speedSlider) {
      speedSlider.addEventListener('input', () => this.updateSpeedDisplay());
      this.updateSpeedDisplay();
    }
  }

  updateSpeedDisplay() {
    const slider = document.getElementById('speedSlider');
    const display = document.getElementById('speedValue');
    if (slider && display) {
      display.textContent = slider.value + 'ms';
    }
  }

  startAnimation(generateStepsFn, showStepFn, resetFn) {
    if (this.isAnimating) {
      this.isAnimating = false;
      clearTimeout(this.animationTimer);
      return;
    }

    if (this.currentStep === 0 || this.currentStep >= this.animationSteps.length) {
      resetFn();
      this.animationSteps = generateStepsFn();
    }

    this.isAnimating = true;
    this.animate(showStepFn);
  }

  animate(showStepFn) {
    if (!this.isAnimating || this.currentStep >= this.animationSteps.length) {
      this.isAnimating = false;
      return;
    }

    showStepFn(this.animationSteps[this.currentStep]);
    this.currentStep++;

    const speed = document.getElementById('speedSlider').value;
    this.animationTimer = setTimeout(() => this.animate(showStepFn), parseInt(speed));
  }

  prevStep(generateStepsFn, showStepFn) {
    if (this.animationSteps.length === 0) {
      this.animationSteps = generateStepsFn();
    }

    if (this.currentStep > 0) {
      this.currentStep--;
      showStepFn(this.animationSteps[this.currentStep]);
    }
  }

  nextStep(generateStepsFn, showStepFn) {
    if (this.animationSteps.length === 0) {
      this.animationSteps = generateStepsFn();
    }

    if (this.currentStep < this.animationSteps.length) {
      showStepFn(this.animationSteps[this.currentStep]);
      this.currentStep++;
    }
  }

  reset(resetFn) {
    this.isAnimating = false;
    clearTimeout(this.animationTimer);
    this.currentStep = 0;
    this.animationSteps = [];
    resetFn();
  }
}

class CodeDisplay {
  constructor() {
    this.javaCode = [];
  }

  setCode(codeArray) {
    this.javaCode = codeArray;
    this.initCodeDisplay();
  }

  initCodeDisplay() {
    const codeDisplay = document.getElementById('codeDisplay');
    if (!codeDisplay) return;

    codeDisplay.innerHTML = this.javaCode
      .map(lineObj => {
        return `
          <div class="code-line" id="line-${lineObj.line}">
            <span class="line-number">${lineObj.line}</span>
            <span>${lineObj.content}</span>
          </div>
        `;
      })
      .join('');
  }

  highlightCodeLines(lines) {
    if (!this.javaCode.length) return;

    this.javaCode.forEach(lineObj => {
      const lineEl = document.getElementById(`line-${lineObj.line}`);
      if (lineEl) {
        if (lines.includes(lineObj.line)) {
          lineEl.classList.add('highlight');
        } else {
          lineEl.classList.remove('highlight');
        }
      }
    });
  }
}

const animationController = new AnimationController();
const codeDisplay = new CodeDisplay();

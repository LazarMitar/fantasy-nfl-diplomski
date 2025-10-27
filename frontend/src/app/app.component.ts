import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-container">
      <header>
        <h1>NFL Fantasy League</h1>
      </header>
      <main>
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
    }
    header {
      background-color: #1a1a1a;
      color: white;
      padding: 1rem;
      text-align: center;
    }
    main {
      padding: 2rem;
    }
  `]
})
export class AppComponent {
  title = 'fantasy-football-frontend';
}


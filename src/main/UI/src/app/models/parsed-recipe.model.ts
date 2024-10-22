import { SafeHtml } from '@angular/platform-browser';

export interface ParsedRecipe {
  id: number;
  title: SafeHtml;
  ingredients: SafeHtml;
  instructions: SafeHtml;
}

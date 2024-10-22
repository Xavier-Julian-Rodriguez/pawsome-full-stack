export class Recipe {
 id:number = 0;
 title:string = "";
 ingredients:string = "";
 instructions:string = "";

  constructor(id: number, title: string, ingredients: string, instructions: string) {
    this.id = id;
    this.title = title;
    this.ingredients = ingredients;
    this.instructions = instructions;
  }
}

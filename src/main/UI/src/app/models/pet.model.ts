export class Pet {
  id: number;
  name: string;
  age: number;
  species: string;
  image: string | null;
  imageBase64?: string;

  constructor(id: number, name: string, age: number, species: string, image: string, imageBase64: string) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.species = species;
    this.image = image;
    this.imageBase64 = imageBase64;
  }
}

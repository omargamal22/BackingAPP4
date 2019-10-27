package com.example.backingapp4.Model.Meals;



 public class Ingredient {
    public int id;
    private String quantity;
    private String measure;
    private String ingredient;

     public String getQuantity() {
         return quantity;
     }

     public void setQuantity(String quantity) {
         this.quantity = quantity;
     }

     public String getMeasure() {
         return measure;
     }

     public void setMeasure(String measure) {
         this.measure = measure;
     }

     public String getIngredient() {
         return ingredient;
     }

     public void setIngredient(String ingredient) {
         this.ingredient = ingredient;
     }

     public Ingredient(String quantity, String measure, String ingredient) {

         this.quantity = quantity;
         this.measure = measure;
         this.ingredient = ingredient;
     }
 }

# Recipe API

## Summary
The idea here is to create a microservice that allows users to manage their favorite recipes. It allows adding, updating, removing, and fetching of recipes. Additionally, users are able to filter available recipes
based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings per recipe
3. Specific ingredients (either includes or excludes)
4. Search within the instructions

For example, the API should be able to handle the following search requests:
* All vegetarian recipes
* Recipes that can serve 4 persons and have “potatoes” as an ingredient
* Recipes without “salmon” as an ingredient that has “oven” in the instructions


## Running the API

* git clone https://github.com/brandon-pickup-bbd/Recipes.git
* cd Recipes
* docker-compose up
	* If you have trouble with this, try pulling the image and then retry the command: docker pull brandonpickupbbd/recipes-microservice-recipes:0.0.1-SNAPSHOT
* Open the following URL: http://localhost:8080/recipes
* It is also possible to interact with the API using the [Swagger UI](http://localhost:8080/swagger-ui/index.html#/) or with Postman using [this collection](https://github.com/brandon-pickup-bbd/Recipes/blob/main/RecipeApi.postman_collection.json) at the root of the repository (which has predefined requests, ready for you to use)

## Documentation

* [OpenAPI Recipe Definition](https://github.com/brandon-pickup-bbd/Recipes/blob/main/RecipeApi.postman_collection.json)
* [Postman Collection](https://github.com/brandon-pickup-bbd/Recipes/blob/main/RecipeApi.postman_collection.json) 

## Discussion Points

### Overview
* Made use of spring boot as it is super fast to stand up a java microservice
* The dataset felt relational, so I made use of an H2 relational database.
	* H2 is an in-memory database, it is light-weight, and sufficient for this assignment
	* Used JPA to interface with the H2 database
* Bundled in a docker image to simplify use by others

### Enhancements
* The recipe collection fetch is in serious need of some form of SearchCriteria implementation. The current implementation is most certainly a work-around to meet the brief, but there must be better ways to do it. For now, with the time allotted, I'll leave as is 
* The database design is not the most streamlined. I'm not sure this was the focus of the exercise though, so I didn't spend too much time on it
* I've done no optimization on the docker image at all to reduce it's size, simply added some configuration to the pom.xml file

### Omissions
The following was omitted as I felt they were not in scope for the exercise:
* API validation 
* API security
* Unit tests (Integration tests are however available for the RecipeController)
define([
    'dojo/_base/declare',  	
    "ecm/model/Request",
], function(declare, Request) {
    return declare("WebContent.ApiTest",[],{
        run: async function(searchQuery){
            console.log("WebContent.ApiTest run function called\AI ------------------")
            query = this.keepOrRandom(searchQuery);
            console.log(query);

            const response = await fetch(`https://pokeapi.co/api/v2/pokemon/${query}/`);
            if(response.status!=200){
                this.showError(query, "icmPageWidget");
                return;
            }
            const {name, types} = await response.json();
            const primaryType = types[0].type.name;
            const location = "/Pokemons";

            // const result = Request.invokePluginServiceSynchronous("ICMWidgetPackage", "InsertPokemonService", {
            //     requestParams: {name, location, primaryType}
            // });
            // console.log(result);
            this.showPokemonName(name, "icmPageWidget");
        },

        showPokemonName: function(name, classname){
            console.log("showPokemonName");
            var objTo = document.getElementsByClassName(classname);
            if (!document.getElementById("SuperContainerId")){
                var container = document.createElement("div");
                container.id = "SuperContainerId";
                objTo[0].appendChild(container);
            }

            document.getElementById('SuperContainerId').innerHTML +=
            `<h3>Pokemon ${name} has been added</h3>`;
        },

        showError: function(name, classname){
            console.log("showError");
            var objTo = document.getElementsByClassName(classname);

            var container = document.createElement("div");
            container.id = "SuperContainerId";
            objTo[0].appendChild(container);
            document.getElementById('SuperContainerId').innerHTML +=
            `<h3>Pokemon ${name} was not found</h3>`;
        },

        keepOrRandom: function(name){
            if(name || name !==""){
                return name;
            }else{
                return this.getRandomPokemonNumber(1010);
            }
        },

        getRandomPokemonNumber: function(totalAmountOfOptions){
            return Math.floor(Math.random() * totalAmountOfOptions + 1);
        }

    })
    
});
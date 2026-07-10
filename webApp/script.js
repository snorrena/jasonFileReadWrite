(function () {
    document.getElementById("submit_button").addEventListener("click", postFormData);
    document.getElementById("form1").addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            postFormData()
        }
    });

    function postFormData() {
        console.log("postFormData");
        if (validFormData()) {
            //create a data payload from the web form element
            const formElement = document.getElementById("form1");
            const formData = new FormData(formElement);
            const jsonObject = Object.fromEntries(formData.entries());
            const jsonString = JSON.stringify(jsonObject);

            //use the fetch method to post the form data and send to the java spark api
            fetch("http://127.0.0.1:4567/post-user", {
                method: "post", body: jsonString,
            }).then((response) => {
                console.log(response);
                //clear the input fields after data post
                document.getElementById("name_input").value = "";
                document.getElementById("email_input").value = "";

                //load a new list of users for display in the dom
                loadUserData().then(r => console.log("user data loaded after post"));
            });
        }
    }

    //validates the form data based on existing input values
    function validFormData() {
        let name = document.getElementById("name_input").value;
        let email = document.getElementById("email_input").value;
        return !!(name && validateEmail(email));
    }

    function validateEmail(email) {
        // A clean, permissive regex that catches typos without blocking unusual valid emails
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    //get request to fetch a list of users from the java spark api
    async function loadUserData() {
        await fetch("http://127.0.0.1:4567/get-users") //fetch returns a promise
            .then((res) => {
                if (res.ok) {
                    console.log("fetch successful");
                    res.json().then((data) => printDataToDom(data)); //if response is ok, use then to extract the promise data and pass to the dom
                } else {
                    console.log("fetch not successful");
                }
            });
    }

    loadUserData().then(r => console.log("user data loaded"));

    function printDataToDom(data) {
        console.log(data);
        let userArray = JSON.parse(data);
        let id_input = document.getElementById("id");
        //get the max id value to pre-populate the next value increment. the reduce method return the user with the highest id value
        const max = userArray.reduce((prev, current) => prev.id > current.id ? prev : current);
        //set the id input value to the next value
        id_input.value = max.id + 1;

        //iterate over the user list creating html list items that will be inserted into the dom
        let output = "";
        userArray.forEach((user) => {
            output += `<li>id:\t${user.id}, name:\t${user.name}, email:\t${user.email}</li>`;
        });
        let user_data_ul = document.getElementById("user_data_ul");
        user_data_ul.innerHTML = output;
    }
})();

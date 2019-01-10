getAllUsers();

function getAllUsers() {
    fetch("http://localhost:8081/users").then(function (response) {
        return response.json();
    }).then(function (json) {
        console.log(json);
        showUsers(json);
    });
}


function createUser() {
    var name = document.getElementById('name_field').value;
    var email = document.getElementById('email_field').value;
    var password = document.getElementById('password_field').value;
    var phone = document.getElementById('phone_field').value;
    var age = document.getElementById('age_field').value;

    fetch('http://localhost:8081/createUser', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "name": name,
            "email": email,
            "password": password,
            "phone": phone,
            "age": age
        })
    }).then(function (response) {
        if (response.ok) {
            getAllUsers();
        } else {
            alert("User already exist");
        }
    })
}

function deleteUser(email) {
    console.log("Email: " + email);
    fetch('http://localhost:8081/deleteUser', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "email": email
        })
    }).then(function (response) {
        if (response.ok) {
            getAllUsers();
        } else {
            alert("User could not be found");
        }
    });
}

function showUsers(json) {
    var table_body = '';
    json.forEach(function (object) {
        table_body += formatUserRow(object);
    });
    document.getElementById('table_body').innerHTML = table_body;
}

function formatUserRow(user) {
    return (
        "<tr>\n" +
        "   <td>" + user['name'] + "</td>\n" +
        "   <td>" + user['email'] + "</td>\n" +
        "   <td>" + user['phone'] + "</td>\n" +
        "   <td>" + user['age'] + "</td>\n" +
        "   <td><button onclick=deleteUser(\"" + user['email'] + "\") type=\"button\" class=\"nes-btn is-error\">Delete</button></td>\n" +
        "</tr>"
    );
}
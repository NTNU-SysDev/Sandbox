getAllUsers();

function getAllUsers() {

    /*var header = new Headers({
        'Access-Control-Allow-Origin': '*',
        'Content-Type': "application/json"
    });

    var request = new Request("http://localhost:8081/users", {
        method: "GET",
        header: header,
        mode: "cors"
    });*/

    fetch("http://localhost:8081/users").then(function (response) {
        return response.json();
    }).then(function (json) {
        console.log(json);
        showUsers(json);
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
        "   <td><button type=\"button\" class=\"nes-btn is-error\">Delete</button></td>\n" +
        "</tr>"
    );
}
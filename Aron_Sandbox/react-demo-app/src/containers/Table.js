import React from "react";
import Row from '../components/Row';

class Table extends React.Component {

    constructor() {
        super();
        this.state = {
            users: [],
        };
        this.deleteExistingUser = this.deleteExistingUser.bind(this);
    }

    componentDidMount() {
        fetch("http://localhost:8080/users")
            .then(response => {
                return response.json();
            }).then(data => {
            let users = data.map((row) => {
                return <Row key={row.email} name={row.name} email={row.email}
                        phone={row.phone} age={row.age}/>
            });
            this.setState({users: users});
        });
    }

    deleteExistingUser(email) {
        fetch('http://localhost:8080/deleteUser', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "email": email
            })
        }).then(function (response) {
            if (response.ok) {
                window.location.reload();
            } else {
                alert("User could not be found");
            }
        });
    }

    render() {
        return (
            <table className="highlight centered">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Age</th>
                    <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                {this.state.users}
                </tbody>
            </table>
        );
    }
}

export default Table;
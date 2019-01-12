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
        const api_url = process.env.REACT_APP_BACKEND_URL + "/users";
        fetch(api_url)
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
        const api_url = process.env.REACT_APP_BACKEND_URL + "/deleteUser";
        fetch(api_url, {
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
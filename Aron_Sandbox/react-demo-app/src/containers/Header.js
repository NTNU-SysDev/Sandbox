import React from 'react';
import Modal from 'react-responsive-modal';
import logo from '../logo.svg';
import '../css/App.css';

class Header extends React.Component {

    constructor() {
        super();
        this.state = {
            open: false
        };
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.createNewUser = this.createNewUser.bind(this);
    }

    openModal() {
        this.setState({open: true});
    }

    closeModal() {
        this.setState({open: false});
    }

    createNewUser() {
        const name = document.getElementById('name_field').value;
        const email = document.getElementById('email_field').value;
        const phone = document.getElementById('phone_field').value;
        const age = document.getElementById('age_field').value;

        const api_url = process.env.REACT_APP_BACKEND_URL + "/createUser";
        fetch(api_url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "name": name,
                "email": email,
                "phone": phone,
                "age": age
            })
        }).then(function (response) {
            if (response.ok) {
                alert("User created!");
                window.location.reload();
            } else {
                alert("User already exist");
            }
        })
    }

    render() {
        const {open} = this.state;
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1>This is a simple Demo React App</h1>
                    <button type="button" onClick={this.openModal} className="green waves-effect waves-light btn-large center">
                        <i className="material-icons right">add_circle</i> Add User
                    </button>
                    <Modal open={open} onClose={this.closeModal} center>
                        <p className="modal-header">Add User</p>
                        <div className="row">
                            <form className="col s12">
                                <div className="row">
                                    <div className="input-field col s12">
                                        <label htmlFor="name">Name</label>
                                        <input id="name_field" type="text" className="validate"/>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="input-field col s12">
                                        <label htmlFor="email">Email</label>
                                        <input id="email_field" type="text" className="validate"/>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="input-field col s6">
                                        <label htmlFor="phone">Phone</label>
                                        <input id="phone_field" type="text" className="validate"/>
                                    </div>
                                    <div className="input-field col s6">
                                        <label htmlFor="age">Age</label>
                                        <input id="age_field" type="number" className="validate"/>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col s12 m6 l6 center-align">
                                        <button type="button" className="btn waves-effect btn-large waves-light red"
                                        onClick={this.closeModal}>
                                            Close
                                        </button>
                                    </div>
                                    <div className="col s12 m6 l6 center-align">
                                        <button type="button" className="green btn waves-effect btn-large waves-light"
                                        onClick={this.createNewUser}>
                                            Create
                                            <i className="material-icons right">add_circle</i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </Modal>
                </header>
            </div>
        );
    }
}

export default Header;

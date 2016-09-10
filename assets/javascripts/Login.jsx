import React from 'react';
import {connect} from "react-redux"
import {Modal, Form, Input,
        Button, FormControl, ControlLabel,
        FormGroup} from 'react-bootstrap';
import * as act from "./actions/actions"
import ReactDOM from 'react-dom';

@connect((store) => {
    return {
        user: store.login.user,
    };
})
export default class Login extends React.Component {
    registerUser() {
        let name = ReactDOM.findDOMNode(this.refs.userNameInput).value;
        this.props.dispatch(act.registerUser({name: name}));
    }

    render() {
        return (<Modal show={!this.props.user} onHide={this.close}>
                    <Modal.Header closeButton={false}>
                        <Modal.Title>Modal heading</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <FormGroup>
                            <ControlLabel>User name</ControlLabel>
                            <FormControl
                                name="refreshPeriod"
                                ref="userNameInput"
                                type="text"
                            />
                        </FormGroup>
                        <Button onClick={::this.registerUser} className="center-block">Close</Button>
                    </Modal.Body>
                </Modal>)
    }
}
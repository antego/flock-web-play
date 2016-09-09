import React from 'react';
import { connect } from "react-redux"

@connect((store) => {
    return {
        user: store.user,
    };
})
export default class App extends React.Component {
    render() {
        return (<Modal
                    show={!this.props.user}
                    onHide={close}
                    container={this}
                    aria-labelledby="contained-modal-title"
                >
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title">Contained Modal</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form bsStyle="inline" callback={this.handleLogin}>
                            <Input name="email" type="email" placeholder="Email" required={true}/>
                            <Input name="password" type="password" placeholder="Password / Blank"
                                   required={true} minLength={5}/>
                            <Button onClick={this.handleLogin} type="#" bsStyle="success">
                                Log in / Recover Pass
                            </Button>
                        </Form>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button onClick={close}>Close</Button>
                    </Modal.Footer>
                </Modal>)
    }
}
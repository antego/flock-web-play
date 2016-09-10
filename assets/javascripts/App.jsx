import 'babel-polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import Map from './Map.jsx'
import Login from './Login.jsx'
import { Button, Panel, FormControl, FormGroup, Form } from 'react-bootstrap';
import { Provider } from 'react-redux';
import store from './store'
import { connect } from "react-redux"

@connect((store) => {
    return {
        user: store.login.user,
    };
})
export default class App extends React.Component {

    applyRefreshPeriod() {
        console.log(ReactDOM.findDOMNode(this.refs.refreshPeriodInput).value);

    }

    submit(e) {
        e.preventDefault();
        console.log(e);

    }

    render() {
        return (<div>
                    {/*<Login />*/}
                    <div className="mapContainer">
                        <Map />
                    </div>
                    <Panel className="taskContainer">
                        <Form onSubmit={::this.submit}>
                            <FormGroup>
                                <FormControl
                                    name="refreshPeriod"
                                    ref="refreshPeriodInput"
                                />
                            </FormGroup>
                            <Button type="button" onClick={::this.applyRefreshPeriod}>
                                Apply
                            </Button>
                            {/*<Button type="submit">*/}
                                {/*Submit*/}
                            {/*</Button>*/}
                        </Form>
                    </Panel>
                </div>);
    }
}

ReactDOM.render(<Provider store={store}>
                    <App/>
                </Provider>, document.getElementById('app'));

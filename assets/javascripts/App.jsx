import 'babel-polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import Map from './Map.jsx'
import Create from './Create.jsx';
import { Button, Panel, FormControl, FormGroup, Form } from 'react-bootstrap';
import { Provider } from 'react-redux';
import store from './store'
import * as act from "./actions/actions"


export default class App extends React.Component {
    // handleMapClick(e){
    //     this.props.dispatch(act.mapClickFrom());
    //     if (this.state.pickFromPnt) {
    //         this.setState({fromPnt: e.latlng})
    //     } else if (this.state.pickToPnt) {
    //         this.setState({toPnt: e.latlng})
    //     }
    // }

    // handleCreateOrder() {
    //     this.props.dispatch(act.setPickToMode())
    // }
    //
    // handleSubmitOrder(order) {
    //     console.log(order);
    //     this.setState(initialState);
    // }
    //
    // handlePickFromPnt() {
    //     this.setState({pickFromPnt: true, pickToPnt: false})
    // }
    //
    // handlePickToPnt() {
    //     this.setState({pickFromPnt: false, pickToPnt: true})
    // }

    applyRefreshPeriod() {
        console.log(ReactDOM.findDOMNode(this.refs.refreshPeriodInput).value);

    }

    submit(e) {
        e.preventDefault();
        console.log(e);

    }

    render() {
        return (<div>
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

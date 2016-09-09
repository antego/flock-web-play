import 'babel-polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import Map from './Map.jsx'
import Create from './Create.jsx';
import { Button, Panel } from 'react-bootstrap';
import { Provider } from 'react-redux';
import store from './store'
import * as act from "./actions/actions"


export default class App extends React.Component {
    handleMapClick(e){
        this.props.dispatch(act.mapClickFrom());
        if (this.state.pickFromPnt) {
            this.setState({fromPnt: e.latlng})
        } else if (this.state.pickToPnt) {
            this.setState({toPnt: e.latlng})
        }
    }

    handleCreateOrder() {
        this.props.dispatch(act.setPickToMode())
    }

    handleSubmitOrder(order) {
        console.log(order);
        this.setState(initialState);
    }

    handlePickFromPnt() {
        this.setState({pickFromPnt: true, pickToPnt: false})
    }

    handlePickToPnt() {
        this.setState({pickFromPnt: false, pickToPnt: true})
    }

    render() {
        return (<div>

                    <div className="mapContainer">
                        <Map pickFrom={this.state.pickFromPnt}
                             pickTo={this.state.pickToPnt}
                             clickCallback={::this.handleMapClick} />
                    </div>
                    <Panel className="taskContainer">
                        <Button className="btn-primary btn-lg" onClick={::this.handleCreateOrder}>New order</Button>
                        <div>{this.state.pick ? "Choose origin and dest." : null}</div>
                        <Create/>
                    </Panel>
                </div>);
    }
}

ReactDOM.render(<Provider store={store}>
                    <App/>
                </Provider>, document.getElementById('app'));

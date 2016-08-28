import 'babel-polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import Map from './Map.jsx'
import Task from './Task.jsx'

let initialState = {
    pick: false,
    pickFromPnt: false,
    pickToPnt: false,
    fromPnt: null,
    toPnt: null,
};

export default class App extends React.Component {

    constructor() {
        super();
        this.state = initialState;
    }

    handleMapClick(e){
        if (this.state.pickFromPnt) {
            this.setState({fromPnt: e.latlng})
        } else if (this.state.pickToPnt) {
            this.setState({toPnt: e.latlng})
        }
    }

    handleCreateOrder() {
        this.setState({pick: true, pickFromPnt: true})
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
                    <div className="taskContainer">
                        <button onClick={::this.handleCreateOrder}><h1>New order</h1></button>
                        <p>{this.state.pick ? "Choose origin and dest." : null}</p>

                        <Task pickToPntCb={::this.handlePickToPnt}
                              pickFromPntCb={::this.handlePickFromPnt}
                                submitOrder={::this.handleSubmitOrder}
                              fromPnt={this.state.fromPnt}
                              toPnt={this.state.toPnt}
                              pickFromPnt={this.state.pickFromPnt}
                              pickToPnt={this.state.pickToPnt}
                        />
                    </div>
                </div>);
    }
}

ReactDOM.render(<App/>, document.getElementById('app'));

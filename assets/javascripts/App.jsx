import 'babel-polyfill'
import React from 'react';
import ReactDOM from 'react-dom';
import Map from './Map.jsx'
import { Button, Panel, FormControl, FormGroup, Form } from 'react-bootstrap';
import { Provider } from 'react-redux';
import store from './store'
import { connect } from "react-redux"

export default class App extends React.Component {
    render() {
        return (<div>
                    <div className="mapContainer">
                        <Map />
                    </div>
                </div>);
    }
}

ReactDOM.render(<Provider store={store}>
                    <App/>
                </Provider>, document.getElementById('app'));

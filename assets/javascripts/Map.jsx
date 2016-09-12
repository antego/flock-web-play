import 'babel-polyfill'
import React from 'react';
import {render} from 'react-dom';
import {Map, CircleMarker, Popup, TileLayer, Polyline} from 'react-leaflet';
import {connect} from "react-redux"
import * as act from "./actions/actions"
import axios from "axios"

const position = [47.2, 38.9];
@connect((store) => {
    return {
        users: store.map.users,
        traces: store.map.userTraces,
    };
})
export default class MyMap extends React.Component {
    componentDidMount() {
        this.refreshIntervalId = setInterval(() => {
            axios.get('/users')
              .then((response) => {
                this.props.dispatch(act.refreshUsers(response.data));
              })
              .catch((error) => {
                console.log(error);
              });
        }, 1000);
    }

    componentWillUnmount() {
        clearInterval(this.refreshIntervalId);
    }

    arrayClone(arr) {
        return arr.map((arr) => {
            return arr.slice();
        });
    }

    render() {
        let markers = this.props.users.map((user, i) => {
            let position = {lat: Number.parseFloat(user.lat), lng: Number.parseFloat(user.lng)};
            if (Number.isNaN(position.lat) || Number.isNaN(position.lng)) {
                return null
            }
            return (<CircleMarker center={position} key={i} >
                        <Popup>
                            <span><b>{user.name}</b></span>
                        </Popup>
                    </CircleMarker>)
        });

        let polylines = Array.from(this.props.traces.values()).map((arr, i) => {
            arr = ::this.arrayClone(arr);
            return (<Polyline color='lime' positions={arr} key={i} />);
        });

        return (<Map center={position} zoom={13} ref="map">
                    <TileLayer
                        url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    />
                    {polylines}
                    {markers}
                </Map>);
    }

}




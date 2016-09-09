import React from 'react';
import { render } from 'react-dom';
import {
    Circle,
    FeatureGroup,
    LayerGroup,
    LayersControl,
    Map,
    Marker,
    Popup,
    Rectangle,
    TileLayer,
} from 'react-leaflet';
import { connect } from "react-redux"
import * as act from "./actions/actions"
import axios from "axios"

const position = [51.505, -0.09];
@connect((store) => {
    return {
        users: store.map.users,
        refreshPeriodMillis: store.map.refreshPeriodMillis,
    };
})
export default class MyMap extends React.Component {
    // handleMapClick(e) {
    //     if (this.props.pickMode === "PICK_FROM") {
    //         this.props.dispatch(act.fromPointSelected(e.latlng));
    //     } else if (this.props.pickMode === "PICK_TO") {
    //         this.props.dispatch(act.toPointSelected(e.latlng));
    //     }
    // }
    componentDidMount() {
        this.refreshIntervalId = setInterval(() => {
            axios.get('/users')
              .then((response) => {
                console.log(response);
                this.props.dispatch(act.refreshUsers(response.data));
              })
              .catch((error) => {
                console.log(error);
              });
        }, this.props.refreshPeriodMillis);
    }

    componentWillUnmount() {
        clearInterval(this.refreshIntervalId);
    }


    render() {
        let markers = this.props.users.map((user, i) => {
            let position = {lat: Number.parseFloat(user.lat), lng: Number.parseFloat(user.lng)};
            return <Marker position={position} key={i}/>;
        });

        return (<Map center={position} zoom={13} ref="map">
                    <TileLayer
                        url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    />

                    {markers}

                </Map>);
    }
}




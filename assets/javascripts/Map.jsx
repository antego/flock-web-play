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

const position = [51.505, -0.09];
export default class MyMap extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fromPntPos: null,
            toPntPos: null,
        }
    }

    componentWillReceiveProps(nextProps) {
        if (!nextProps.pickFrom && !nextProps.pickTo) {
            this.state.fromPntPos = null;
            this.state.toPntPos = null;
        }
    }

    handleMapClick(e) {
        if (this.props.pickFrom) {
            this.setState({fromPntPos: e.latlng})
        } else if (this.props.pickTo) {
            this.setState({toPntPos: e.latlng})
        }
        this.props.clickCallback(e);
    }


    render() {
        let fromMarker = this.state.fromPntPos ? <Marker position={this.state.fromPntPos} /> : null;
        let toMarker = this.state.toPntPos ? <Marker position={this.state.toPntPos} /> : null;

        return (<Map center={position} zoom={13} onClick={::this.handleMapClick}>
                    <TileLayer
                        url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    />
                    <LayerGroup>
                        {fromMarker}
                        {toMarker}
                    </LayerGroup>
                </Map>);
    }
}




import React from "react";

export default class Task extends React.Component {
    handleNewOrder() {
        this.props.submitOrder({
            fromPnt: this.props.fromPnt,
            toPnt: this.props.toPnt});
    }

    render() {
        let fromPntCoords;
        let toPntCoords;
        if (this.props.fromPnt) {
            fromPntCoords = (<p> {this.props.fromPnt.lat}  {this.props.fromPnt.lng}</p>);
        }
        if (this.props.toPnt) {
            toPntCoords = (<p> {this.props.toPnt.lat}  {this.props.toPnt.lng}</p>);
        }
        return (<div>
            <button disabled={this.props.pickFromPnt} onClick={this.props.pickFromPntCb}>Pick origin point</button>
            {fromPntCoords}
            <br/>
            <button disabled={this.props.pickToPnt} onClick={this.props.pickToPntCb}>Pick destination point</button>
            {toPntCoords}

            <button onClick={::this.handleNewOrder}>Submit new order</button>
        </div>);
    }
}
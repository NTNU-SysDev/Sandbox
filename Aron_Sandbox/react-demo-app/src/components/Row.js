import React from 'react';

const row = (props) => (
    <tr>
        <td>{props.name}</td>
        <td>{props.email}</td>
        <td>{props.phone}</td>
        <td>{props.age}</td>
        <td>
            <button className="red waves-effect waves-light btn">
                Delete
            </button>
        </td>
    </tr>
);

export default row;
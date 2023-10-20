import React from "react";
import "./Spinner.css";
import PropTypes from "prop-types";
/* Taken from https://loading.io/css/  */

const Spinner = ({ color = "green", show = true }) => {
  return (
    show && (
      <div className="lds-ellipsis">
        <div style={{ background: color }} />
        <div style={{ background: color }} />
        <div style={{ background: color }} />
        <div style={{ background: color }} />
      </div>
    )
  );
};

Spinner.propTypes = {
  color: PropTypes.string,
  show: PropTypes.bool
};

export default Spinner;

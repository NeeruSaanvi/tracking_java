import React from "react";
import PropTypes from "prop-types";

const Alert = props => {
  const { show = false, type, children } = props;
  const className = getClassName(type);
  return show && <div className={`alert ${className}`}>{children}</div>;
};

function getClassName(type) {
  switch (type) {
    case "failure":
      return "alert-danger";
    case "success":
      return "alert-success";
    default:
      return "alert-danger";
  }
}

Alert.propTypes = {
  children: PropTypes.string.isRequired,
  show: PropTypes.bool.isRequired,
  type: PropTypes.string.isRequired
};

export default Alert;

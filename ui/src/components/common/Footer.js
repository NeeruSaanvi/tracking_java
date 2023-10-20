import React from "react";
import PropTypes from "prop-types";

const Footer = props => {
  return (
    <footer className="footer">
      <strong>Tracker</strong> v1.0.0 &copy; Copyright {props.year}
    </footer>
  );
};

Footer.propTypes = {
  year: PropTypes.string.isRequired
};

export default Footer;

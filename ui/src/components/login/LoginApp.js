import React, { useState, useEffect } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import Alert from "../common/Alert";
import { login } from "./loginActions";
import Spinner from "../common/Spinner";

function LoginApp({ login, loading = false, error }) {
  useEffect(() => {
    document.onkeypress = e => {
      if (e.keyCode === 13) {
        onSubmit(e);
      }
    };
  }, []);

  const [username, setUsername] = useState({});
  const [password, setPassword] = useState({});

  const handleUsernameChange = e => {
    setUsername(e.target.value);
  };

  const handlePasswordChange = e => {
    setPassword(e.target.value);
  };

  const onSubmit = e => {
    e.stopPropagation();
    e.preventDefault();

    const username1 = document.getElementById("username").value;
    const password1 = document.getElementById("inputPassword3").value;

    login(username1, password1)
      .then(() => {})
      .catch(() => {});
  };

  return (
    <div className="container-fluid">
      <div className="brand-logo">
        <img src="images/people/110/guy-5.jpg" alt="guy" />
      </div>

      <div className="row">
        <h1>Account Access</h1>

        <div className="col-sm-4 col-sm-offset-4">
          <div className="panel panel-default">
            <div className="panel-body">
              <Alert
                show={error !== null}
                type={error !== null ? "failure" : "success"}
              >
                Invalid User or Password
              </Alert>

              <form role="form" action="#" id="loginForm" onSubmit={onSubmit}>
                <div className="form-group">
                  <div className="input-group">
                    <span className="input-group-addon">
                      <i className="fa fa-user" />
                    </span>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="Username"
                      name="username"
                      id="username"
                      onChange={handleUsernameChange}
                    />
                  </div>
                </div>
                <div className="form-group">
                  <div className="input-group">
                    <span className="input-group-addon">
                      <i className="fa fa-shield" />
                    </span>
                    <input
                      type="password"
                      className="form-control"
                      id="inputPassword3"
                      placeholder="Password"
                      name="password"
                      onChange={handlePasswordChange}
                    />
                  </div>
                </div>
                {loading ? (
                  <Spinner />
                ) : (
                  <div className="text-center">
                    <a href="#" onClick={onSubmit} className="btn btn-success">
                      Login <i className="fa fa-fw fa-unlock-alt" />
                    </a>
                    <input type="submit" style={{ display: "none" }} />
                  </div>
                )}
              </form>
            </div>
          </div>

          <a href="#" className="forgot-pass">
            Forgot your Password?
            <i className="fa fa-question-circle" />
          </a>
        </div>
      </div>
    </div>
  );
}

LoginApp.propTypes = {
  login: PropTypes.func.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string
};

function mapStateToProps(state) {
  const loading = state.apiCallsInProgress > 0;
  const { error } = state;
  return {
    loading,
    error
  };
}

const mapDispatchToProps = {
  login
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LoginApp);

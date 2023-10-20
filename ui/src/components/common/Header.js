import React from "react";
import PropTypes from "prop-types";

const Header = props => {
  return (
    <div className="navbar navbar-default navbar-fixed-top" role={props.role}>
      <div className="container-fluid">
        <div className="navbar-header">
          <a
            href="#sidebar-menu"
            data-toggle="sidebar-menu"
            data-effect="st-effect-3"
            className="toggle pull-left visible-xs"
          >
            <i className="fa fa-bars" />
          </a>

          <a
            href="#sidebar-chat"
            data-toggle="sidebar-menu"
            className="toggle pull-right"
          >
            <i className="fa fa-comments" />
          </a>

          <button
            type="button"
            className="navbar-toggle"
            data-toggle="collapse"
            data-target="#collapse"
          >
            <span className="sr-only">Toggle navigation</span>
            <span className="icon-bar" />
            <span className="icon-bar" />
            <span className="icon-bar" />
          </button>
          <a
            href="index.html"
            className="navbar-brand hidden-xs navbar-brand-primary"
          >
            ThemeKit
          </a>
        </div>
        <div className="navbar-collapse collapse" id="collapse">
          <form className="navbar-form navbar-left hidden-xs" role="search">
            <div className="search-2">
              <div className="input-group">
                <input
                  type="text"
                  className="form-control form-control-w-150"
                  placeholder="Search .."
                />
                <span className="input-group-btn">
                  <button className="btn btn-primary" type="button">
                    <i className="fa fa-search" />
                  </button>
                </span>
              </div>
            </div>
          </form>
          <ul className="nav navbar-nav navbar-right">
            <li className="dropdown notifications updates hidden-xs hidden-sm">
              <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                <i className="fa fa-bell-o" />
                <span className="badge badge-primary">4</span>
              </a>
              <ul className="dropdown-menu" role="notification">
                <li className="dropdown-header">Notifications</li>
                <li className="media">
                  <div className="pull-right">
                    <span className="label label-success">New</span>
                  </div>
                  <div className="media-left">
                    <img
                      src="images/people/50/guy-2.jpg"
                      alt="people"
                      className="img-circle"
                      width="30"
                    />
                  </div>
                  <div className="media-body">
                    <a href="#">Adrian D.</a> posted <a href="#">a photo</a> on
                    his timeline.
                    <br />
                    <span className="text-caption text-muted">5 mins ago</span>
                  </div>
                </li>
                <li className="media">
                  <div className="pull-right">
                    <span className="label label-success">New</span>
                  </div>
                  <div className="media-left">
                    <img
                      src="images/people/50/guy-6.jpg"
                      alt="people"
                      className="img-circle"
                      width="30"
                    />
                  </div>
                  <div className="media-body">
                    <a href="#">Bill</a> posted <a href="#">a comment</a> on
                    Adrianss recent <a href="#">post</a>.
                    <br />
                    <span className="text-caption text-muted">3 hrs ago</span>
                  </div>
                </li>
                <li className="media">
                  <div className="media-left">
                    <span className="icon-block s30 bg-grey-200">
                      <i className="fa fa-plus" />
                    </span>
                  </div>
                  <div className="media-body">
                    <a href="#">Mary D.</a> and <a href="#">Michelle</a> are now
                    friends.
                    <p>
                      <span className="text-caption text-muted">1 day ago</span>
                    </p>
                    <a href="">
                      <img
                        className="width-30 img-circle"
                        src="images/people/50/woman-6.jpg"
                        alt="people"
                      />
                    </a>
                    <a href="">
                      <img
                        className="width-30 img-circle"
                        src="images/people/50/woman-3.jpg"
                        alt="people"
                      />
                    </a>
                  </div>
                </li>
              </ul>
            </li>

            <li className="dropdown notifications hidden-xs hidden-sm">
              <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                <i className="fa fa-envelope-o" />

                <span className="badge floating badge-danger">12</span>
              </a>
              <ul className="dropdown-menu">
                <li className="media">
                  <div className="media-left">
                    <a href="#">
                      <img
                        className="media-object thumb"
                        src="images/people/50/guy-2.jpg"
                        alt="people"
                      />
                    </a>
                  </div>
                  <div className="media-body">
                    <div className="pull-right">
                      <span className="label label-default">5 min</span>
                    </div>
                    <h5 className="media-heading">Adrian D.</h5>

                    <p className="margin-none">
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                    </p>
                  </div>
                </li>
                <li className="media">
                  <div className="media-left">
                    <a href="#">
                      <img
                        className="media-object thumb"
                        src="images/people/50/woman-7.jpg"
                        alt="people"
                      />
                    </a>
                  </div>

                  <div className="media-body">
                    <div className="pull-right">
                      <span className="label label-default">2 days</span>
                    </div>
                    <h5 className="media-heading">Jane B.</h5>
                    <p className="margin-none">
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                    </p>
                  </div>
                </li>
                <li className="media">
                  <div className="media-left">
                    <a href="#">
                      <img
                        className="media-object thumb"
                        src="images/people/50/guy-8.jpg"
                        alt="people"
                      />
                    </a>
                  </div>

                  <div className="media-body">
                    <div className="pull-right">
                      <span className="label label-default">3 days</span>
                    </div>
                    <h5 className="media-heading">Andrew M.</h5>
                    <p className="margin-none">
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                    </p>
                  </div>
                </li>
              </ul>
            </li>

            <li className="dropdown user">
              <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                <img
                  src="images/people/110/guy-6.jpg"
                  alt=""
                  className="img-circle"
                />{" "}
                Bill
                <span className="caret" />
              </a>
              <ul className="dropdown-menu" role="menu">
                <li>
                  <a href="#">
                    <i className="fa fa-user" />
                    Profile
                  </a>
                </li>
                <li>
                  <a href="#">
                    <i className="fa fa-wrench" />
                    Settings
                  </a>
                </li>
                <li>
                  <a href="#">
                    <i className="fa fa-sign-out" />
                    Logout
                  </a>
                </li>
              </ul>
            </li>

            <li className="dropdown flags">
              <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                <img
                  src="images/flags/Flag_of_the_United_States.svg"
                  alt="United States"
                />
                <span className="caret" />
              </a>
              <ul className="dropdown-menu min-width-none" role="menu">
                <li className="active text-center">
                  <a href="#">
                    <img
                      src="images/flags/Flag_of_the_United_States.svg"
                      alt="United States"
                    />
                  </a>
                </li>
                <li className="text-center">
                  <a href="#">
                    <img src="images/flags/Flag_of_France.svg" alt="France" />
                  </a>
                </li>
                <li className="text-center">
                  <a href="#">
                    <img src="images/flags/Flag_of_Germany.svg" alt="Germany" />
                  </a>
                </li>
                <li className="text-center">
                  <a href="#">
                    <img src="images/flags/Flag_of_Romania.svg" alt="Romania" />
                  </a>
                </li>
                <li className="text-center">
                  <a href="#">
                    <img src="images/flags/Flag_of_Poland.svg" alt="Poland" />
                  </a>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

Header.propTypes = {
  role: PropTypes.string.isRequired
};

export default Header;

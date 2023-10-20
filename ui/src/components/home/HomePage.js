import React from "react";

const HomePage = () => (
  <div>
    <script id="chat-window-template" type="text/x-handlebars-template">
      <div className="panel panel-default">
        <div
          className="panel-heading"
          data-toggle="chat-collapse"
          data-target="#chat-bill"
        >
          <a href="#" className="close">
            <i className="fa fa-times" />
          </a>
          <a href="#">
            <span className="pull-left">
              <img src="/testImage.jpg" width="40" />
            </span>
            <span className="contact-name">Viral_USer</span>
          </a>
        </div>
        <div className="panel-body" id="chat-bill">
          <div className="media">
            <div className="media-left">
              <img
                src="/testImage.jpg"
                width="25"
                className="img-circle"
                alt="people"
              />
            </div>
            <div className="media-body">
              <span className="message">Feeling Groovy?</span>
            </div>
          </div>
          <div className="media">
            <div className="media-left">
              <img
                src="/testImage.jpg"
                width="25"
                className="img-circle"
                alt="people"
              />
            </div>
            <div className="media-body">
              <span className="message">Yep.</span>
            </div>
          </div>
          <div className="media">
            <div className="media-left">
              <img
                src="/testImage.jpg"
                width="25"
                className="img-circle"
                alt="people"
              />
            </div>
            <div className="media-body">
              <span className="message">This chat window looks amazing.</span>
            </div>
          </div>
          <div className="media">
            <div className="media-left">
              <img
                src="/testImage.jpg"
                width="25"
                className="img-circle"
                alt="people"
              />
            </div>
            <div className="media-body">
              <span className="message">Thanks!</span>
            </div>
          </div>
        </div>
        <input
          type="text"
          className="form-control"
          placeholder="Type message..."
        />
      </div>
    </script>

    <div className="chat-window-container" />

    <div className="st-pusher">
      <div
        className="sidebar left sidebar-size-2 sidebar-offset-0 sidebar-skin-blue sidebar-visible-desktop"
        id="sidebar-menu"
        data-type="collapse"
      >
        <div className="split-vertical">
          <div className="sidebar-block tabbable tabs-icons">
            <ul className="nav nav-tabs">
              <li className="active">
                <a href="#sidebar-tabs-menu" data-toggle="tab">
                  <i className="fa fa-bars" />
                </a>
              </li>
              <li>
                <a href="#sidebar-tabs-2" data-toggle="tab">
                  <i className="fa fa-bar-chart-o" />
                </a>
              </li>
            </ul>
          </div>
          <div className="split-vertical-body">
            <div className="split-vertical-cell">
              <div className="tab-content">
                <div className="tab-pane active" id="sidebar-tabs-menu">
                  <div data-scrollable>
                    <ul className="sidebar-menu sm-icons-right sm-icons-block">
                      <li className="active">
                        <a href="index.html">
                          <i className="fa fa-home" /> <span>Dashboard</span>
                        </a>
                      </li>
                      <li>
                        <a href="email.html">
                          <i className="fa fa-envelope" /> <span>Email</span>
                        </a>
                      </li>
                      <li>
                        <a href="chat.html">
                          <i className="fa fa-comments" /> <span>Chat</span>
                        </a>
                      </li>
                    </ul>

                    <h4 className="category">Components</h4>
                    <ul className="sidebar-menu sm-bordered sm-active-item-bg">
                      <li className="hasSubmenu text-multiple">
                        <a href="#components">
                          <i className="fa fa-circle-o" />
                          <span className="text">
                            <span className="title">Essentials</span>
                            <span className="details">UI Kit</span>
                          </span>
                        </a>
                        <ul id="components">
                          <li>
                            <a href="essential-overview.html">
                              <i className="fa fa-circle-o" />{" "}
                              <span>Overview</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-buttons.html">
                              <i className="fa fa-th" /> <span>Buttons</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-icons.html">
                              <i className="fa fa-paint-brush" />{" "}
                              <span>Icons</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-typography.html">
                              <i className="fa fa-font" />{" "}
                              <span>Typography</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-expandable.html">
                              <i className="fa fa-ellipsis-h" />{" "}
                              <span>Expandable</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-ribbons.html">
                              <i className="fa fa-circle-o" />{" "}
                              <span>Ribbons</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-forms.html">
                              <i className="fa fa-sliders" /> <span>Forms</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-wizards.html">
                              <i className="fa fa-magic" /> <span>Wizards</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-tabs.html">
                              <i className="md md-tab-unselected" />{" "}
                              <span>Tabs</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-nestable.html">
                              <i className="md md-menu" /> <span>Nestable</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-tree.html">
                              <i className="md md-loupe" />{" "}
                              <span>Tree View</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-modals.html">
                              <i className="fa fa-circle-o" />{" "}
                              <span>Modals</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-tables.html">
                              <i className="fa fa-table" /> <span>Tables</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-progress.html">
                              <i className="fa fa-tasks" />{" "}
                              <span>Progress</span>
                            </a>
                          </li>
                          <li>
                            <a href="essential-grid.html">
                              <i className="fa fa-columns" /> <span>Grid</span>
                            </a>
                          </li>
                        </ul>
                      </li>
                      <li className="">
                        <a href="layout-fluid-1-sidebar.html">
                          <i className="md md-tab-unselected" />{" "}
                          <span>Layouts</span>
                        </a>
                      </li>
                      <li className="hasSubmenu">
                        <a href="#submenu-media">
                          <i className="fa fa-photo" /> <span>Media</span>
                        </a>
                        <ul id="submenu-media">
                          <li>
                            <a href="media-gallery.html">
                              <i className="fa fa-camera" />{" "}
                              <span>Gallery</span>
                            </a>
                          </li>
                          <li>
                            <a href="media-carousel.html">
                              <i className="fa fa-circle-o" />{" "}
                              <span>Carousels</span>
                            </a>
                          </li>
                        </ul>
                      </li>
                      <li className="hasSubmenu">
                        <a href="#nav-maps">
                          <i className="fa fa-globe" /> <span>Maps</span>
                        </a>
                        <ul id="nav-maps">
                          <li>
                            <a href="maps-google-themes.html">
                              <i className="fa fa-eyedropper" />{" "}
                              <span>Themes</span>
                            </a>
                          </li>
                          <li>
                            <a href="maps-google-filters.html">
                              <i className="fa fa-map-marker" />{" "}
                              <span>Filters</span>
                            </a>
                          </li>
                          <li>
                            <a href="maps-google-json.html">
                              <i className="fa fa-map-marker" />{" "}
                              <span>JSON</span>
                            </a>
                          </li>
                          <li>
                            <a href="maps-google-pagination.html">
                              <i className="fa fa-map-marker" />{" "}
                              <span>Pagination</span>
                            </a>
                          </li>
                          <li>
                            <a href="maps-google-edit.html">
                              <i className="fa fa-pencil" /> <span>Edit</span>
                            </a>
                          </li>
                          <li>
                            <a href="maps-google-markers.html">
                              <i className="fa fa-map-marker" />{" "}
                              <span>Markers</span>
                            </a>
                          </li>
                          <li>
                            <a href="maps-vector.html">
                              <i className="fa fa-map-marker" />{" "}
                              <span>Vector Maps</span>
                            </a>
                          </li>
                        </ul>
                      </li>
                      <li className="hasSubmenu">
                        <a href="#nav-charts">
                          <i className="fa fa-bar-chart" /> <span>Charts</span>
                        </a>
                        <ul id="nav-charts">
                          <li>
                            <a href="charts-morris.html">
                              <i className="fa fa-bar-chart" />{" "}
                              <span>Morris</span>
                            </a>
                          </li>
                          <li>
                            <a href="charts-flot.html">
                              <i className="fa fa-bar-chart" />{" "}
                              <span>Flot</span>
                            </a>
                          </li>
                        </ul>
                      </li>
                    </ul>

                    <h4 className="category">Other</h4>
                    <ul className="sidebar-menu sm-bordered sm-active-item-bg">
                      <li>
                        <a href="tickets.html">
                          <i className="fa fa-ticket" /> <span>Tickets</span>
                        </a>
                      </li>
                      <li>
                        <a href="appointments.html">
                          <i className="fa fa-calendar" />{" "}
                          <span>Appointments</span>
                        </a>
                      </li>
                    </ul>

                    <div className="sidebar-block equal-padding">
                      <ul className="list-group list-group-menu">
                        <li className="list-group-item">
                          <a href="login.html">
                            <i className="fa fa-fw fa-lock" />{" "}
                            <span>Login</span>
                          </a>
                        </li>
                        <li className="list-group-item">
                          <a href="sign-up.html">
                            <i className="fa fa-fw fa-pencil" />{" "}
                            <span>Sign Up</span>
                          </a>
                        </li>
                      </ul>
                    </div>

                    <h4 className="category">Versions</h4>
                    <div className="sidebar-block text-center">
                      <a
                        className="btn btn-primary btn-block active"
                        href="index.html"
                      >
                        <strong>HTML</strong>
                      </a>
                      <a
                        className="btn btn-primary btn-block"
                        href="../admin-angular/index.html"
                      >
                        <strong>AngularJS</strong>
                      </a>
                      <a
                        className="btn btn-primary btn-block"
                        href="../admin-rtl/index.html"
                      >
                        <strong>RTL</strong>
                      </a>
                    </div>
                  </div>
                </div>

                <div className="tab-pane" id="sidebar-tabs-2">
                  <div data-scrollable>
                    <div className="category">Activity</div>
                    <div className="sidebar-block">
                      <div className="sidebar-feed">
                        <ul>
                          <li className="media news-item">
                            <span className="news-item-success pull-right ">
                              <i className="fa fa-circle" />
                            </span>
                            <span className="pull-left media-object">
                              <i className="fa fa-fw fa-bell" />
                            </span>
                            <div className="media-body">
                              <a href="" className="text-white">
                                Adrian
                              </a>{" "}
                              just logged in
                              <span className="time">2 min ago</span>
                            </div>
                          </li>
                          <li className="media news-item">
                            <span className="news-item-success pull-right ">
                              <i className="fa fa-circle" />
                            </span>
                            <span className="pull-left media-object">
                              <i className="fa fa-fw fa-bell" />
                            </span>
                            <div className="media-body">
                              <a href="" className="text-white">
                                Adrian
                              </a>{" "}
                              just added{" "}
                              <a href="" className="text-white">
                                mosaicpro
                              </a>{" "}
                              as their office
                              <span className="time">2 min ago</span>
                            </div>
                          </li>
                          <li className="media news-item">
                            <span className="pull-left media-object">
                              <i className="fa fa-fw fa-bell" />
                            </span>
                            <div className="media-body">
                              <a href="" className="text-white">
                                Adrian
                              </a>{" "}
                              just logged in
                              <span className="time">2 min ago</span>
                            </div>
                          </li>
                          <li className="media news-item">
                            <span className="pull-left media-object">
                              <i className="fa fa-fw fa-bell" />
                            </span>
                            <div className="media-body">
                              <a href="" className="text-white">
                                Adrian
                              </a>{" "}
                              just logged in
                              <span className="time">2 min ago</span>
                            </div>
                          </li>
                          <li className="media news-item">
                            <span className="pull-left media-object">
                              <i className="fa fa-fw fa-bell" />
                            </span>
                            <div className="media-body">
                              <a href="" className="text-white">
                                Adrian
                              </a>{" "}
                              just logged in
                              <span className="time">2 min ago</span>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </div>

                    <div className="sidebar-block equal-padding">
                      <div
                        className="btn-group btn-group-justified"
                        data-toggle="buttons"
                      >
                        <label className="btn btn-default active">
                          <input
                            type="radio"
                            name="options"
                            id="option1"
                            autoComplete="off"
                          />{" "}
                          <i className="fa fa-envelope" />
                        </label>
                        <label className="btn btn-default">
                          <input
                            type="radio"
                            name="options"
                            id="option2"
                            autoComplete="off"
                          />{" "}
                          <i className="fa fa-lock" />
                        </label>
                        <label className="btn btn-default">
                          <input
                            type="radio"
                            name="options"
                            id="option31"
                            autoComplete="off"
                          />{" "}
                          <i className="fa fa-list" />
                        </label>
                        <label className="btn btn-default">
                          <input
                            type="radio"
                            name="options"
                            id="option32"
                            autoComplete="off"
                          />{" "}
                          <i className="fa fa-group" />
                        </label>
                      </div>
                    </div>

                    <div className="category">Calendar</div>
                    <div className="sidebar-block padding-none">
                      <div className="datepicker" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <ul className="sidebar-menu sm-active-item-bg sm-icons-right sm-icons-block">
            <li>
              <a href="../../../index.html">
                <i className="fa fa-eyedropper" /> <span>Themes</span>
              </a>
            </li>
          </ul>
        </div>
      </div>

      <div className="st-content" id="content">
        <div className="st-content-inner">
          <div className="container-fluid">
            <h1 className="text-headline page-section-heading">Statistics</h1>
            <div className="row" data-toggle="isotope">
              <div className="item col-xs-12 col-md-4">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="btn-group btn-group-justified">
                      <a href="#" className="btn btn-default">
                        Now
                      </a>
                      <a href="#" className="btn btn-white">
                        Yesterday
                      </a>
                    </div>
                  </div>
                  <hr className="margin-none" />
                  <div className="panel-body text-center">
                    <h4 className="text-display-1">&dollar;129,563</h4>
                    <div className="row">
                      <div className="col-md-8 col-md-offset-2">
                        <div className="progress progress-mini">
                          <div
                            className="progress-bar progress-bar-success"
                            role="progressbar"
                            aria-valuenow="55"
                            aria-valuemin="0"
                            aria-valuemax="100"
                            style={{ width: "55%" }}
                          >
                            <span className="sr-only">55% Complete</span>
                          </div>
                        </div>
                      </div>
                      <a href="#" className="btn btn-white">
                        Add <i className="fa fa-plus" />
                      </a>
                    </div>
                  </div>
                  <ul className="list-group">
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <a href="#">
                            <img
                              src="images/people/110/guy-6.jpg"
                              alt="person"
                              className="img-circle width-30"
                            />
                          </a>
                        </div>
                        <div className="media-body">
                          <a href="#" className="text-subhead">
                            Adrian Demian
                          </a>
                        </div>
                        <div className="media-right">
                          <div className="text-subhead">&dollar;12,201</div>
                        </div>
                      </div>
                    </li>
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <a href="#">
                            <img
                              src="images/people/110/woman-7.jpg"
                              alt="person"
                              className="img-circle width-30"
                            />
                          </a>
                        </div>
                        <div className="media-body">
                          <a href="#" className="text-subhead">
                            Suzanne Morris
                          </a>
                        </div>
                        <div className="media-right">
                          <div className="text-subhead">&dollar;11,546</div>
                        </div>
                      </div>
                    </li>
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <a href="#">
                            <img
                              src="images/people/110/guy-9.jpg"
                              alt="person"
                              className="img-circle width-30"
                            />
                          </a>
                        </div>
                        <div className="media-body">
                          <a href="#" className="text-subhead">
                            Jonny Sea
                          </a>
                        </div>
                        <div className="media-right">
                          <div className="text-subhead">&dollar;8,732</div>
                        </div>
                      </div>
                    </li>
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <a href="#">
                            <img
                              src="images/people/110/woman-3.jpg"
                              alt="person"
                              className="img-circle width-30"
                            />
                          </a>
                        </div>
                        <div className="media-body">
                          <a href="#" className="text-subhead">
                            Mary Dawson
                          </a>
                        </div>
                        <div className="media-right">
                          <div className="text-subhead">&dollar;6,732</div>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>

              <div className="item col-xs-12 col-md-4">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="row">
                      <div className="col-md-4 text-center">
                        <p className="text-body-2 text-light margin-none">
                          Total
                        </p>
                        <p className="text-title text-primary margin-none">
                          12,309
                        </p>
                      </div>
                      <div className="col-md-4 text-center">
                        <p className="text-body-2 text-light margin-none">
                          Earned
                        </p>
                        <p className="text-title text-success margin-none">
                          14,309
                        </p>
                      </div>
                      <div className="col-md-4 text-center">
                        <p className="text-body-2 text-light margin-none">
                          Spent
                        </p>
                        <p className="text-title text-danger margin-none">
                          2,000
                        </p>
                      </div>
                    </div>
                  </div>
                  <hr />
                  <div
                    id="area-chart"
                    data-toggle="morris-chart-area"
                    className="height-200"
                  />
                  <div className="btn-group btn-group-footer btn-group-justified">
                    <a href="#" className="btn btn-lg">
                      <i className="fa fa-bell-o" />
                    </a>
                    <a href="#" className="btn btn-lg">
                      <i className="fa fa-calendar" />
                    </a>
                    <a href="#" className="btn btn-lg">
                      <i className="fa fa-plus" />
                    </a>
                  </div>
                </div>
              </div>

              <div className="item col-xs-12 col-md-4">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="media v-middle">
                      <div className="media-left">
                        <a href="#">
                          <img
                            src="images/people/110/guy-6.jpg"
                            alt=""
                            className="img-circle width-40"
                          />
                        </a>
                      </div>
                      <div className="media-body">
                        <a href="#" className="text-subhead link-text-color">
                          Adrian Demian
                        </a>
                        <div className="text-caption text-light">
                          Earned: &dollar;12,300
                        </div>
                      </div>
                      <div className="media-right">
                        <div className="width-100 text-right">
                          <a href="#" className="btn btn-primary btn-sm">
                            {" "}
                            View
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                  <hr className="margin-none" />
                  <div
                    id="bar-chart"
                    data-toggle="morris-chart-bar"
                    className="height-150"
                  />
                </div>
              </div>

              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default text-center">
                  <div className="panel-body">
                    <h3 className="text-center text-headline margin-none">
                      Sales Target:
                      <span className="text-primary">85%</span>
                    </h3>
                  </div>
                  <hr />
                  <div className="panel-body">
                    <div
                      data-percent="85"
                      data-size="95"
                      className="easy-pie inline-block primary"
                      data-scale-color="false"
                      data-track-color="#efefef"
                      data-line-width="6"
                    >
                      <div className="value text-center">
                        <span className="strong">
                          <i className="icon-graph-up-1 fa-3x text-default" />
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="row">
                      <div className="col-xs-6">
                        <h4 className="text-headline margin-none">291</h4>
                        <p className="text-light">
                          <i className="fa fa-circle-o text-success fa-fw" />{" "}
                          Subscribers
                        </p>
                      </div>
                      <div className="col-xs-6">
                        <h4 className="text-headline margin-none">17</h4>
                        <p className="text-light">
                          <i className="fa fa-circle-o text-danger fa-fw" />{" "}
                          Unsubscribers
                        </p>
                      </div>
                    </div>

                    <div className="progress progress-mini">
                      <div
                        className="progress-bar progress-bar-success"
                        style={{ width: "85%" }}
                      >
                        <span className="sr-only">35% Complete (info)</span>
                      </div>
                      <div
                        className="progress-bar progress-bar-danger"
                        style={{ width: "15%" }}
                      >
                        <span className="sr-only">10% Complete (danger)</span>
                      </div>
                    </div>
                  </div>
                  <div className="panel-footer">
                    <div className="text-right">
                      <a href="#" className="btn btn-white">
                        View Activity
                      </a>
                    </div>
                  </div>
                </div>
              </div>

              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div
                      id="world-map-markers"
                      data-toggle="vector-world-map-markers"
                      className="overflow-hidden height-180"
                    />
                  </div>
                  <ul className="list-group">
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <div className="width-30">
                            <i className="fa fa-circle text-red-400" />
                          </div>
                        </div>
                        <div className="media-body">United States</div>
                        <div className="media-right">
                          <div className="text-right">
                            <div
                              className="sparkline-line width-100"
                              sparkheight="20"
                              sparkwidth="100%"
                              data-data="[ 184, 357, 297, 591, 196, 108, 466, 186 ]"
                            />
                          </div>
                        </div>
                      </div>
                    </li>
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <div className="width-30">
                            <i className="fa fa-circle text-blue-400" />
                          </div>
                        </div>
                        <div className="media-body">Europe</div>
                        <div className="media-right">
                          <div className="text-right">
                            <div
                              className="sparkline-line width-100"
                              sparkheight="20"
                              sparkwidth="100%"
                              data-data="[ 184, 357, 297, 591, 196, 108, 466, 186 ]"
                            />
                          </div>
                        </div>
                      </div>
                    </li>
                    <li className="list-group-item">
                      <div className="media v-middle">
                        <div className="media-left">
                          <div className="width-30">
                            <i className="fa fa-circle text-grey-400" />
                          </div>
                        </div>
                        <div className="media-body">Asia</div>
                        <div className="media-right">
                          <div className="text-right">
                            <div
                              className="sparkline-line width-100"
                              sparkheight="20"
                              sparkwidth="100%"
                              data-data="[ 184, 357, 297, 591, 196, 108, 466, 186 ]"
                            />
                          </div>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>

              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="row text-center">
                      <div className="col-md-6">
                        <h4 className="margin-none">Gross Revenue</h4>
                        <p className="text-display-1 text-warning margin-none">
                          102.4k
                        </p>
                      </div>
                      <div className="col-md-6">
                        <h4 className="margin-none">Net Revenue</h4>
                        <p className="text-display-1 text-success margin-none">
                          55k
                        </p>
                      </div>
                    </div>
                  </div>
                  <hr />
                  <div className="panel-body">
                    <div
                      id="line-holder"
                      data-toggle="flot-chart-lines-3"
                      className="flotchart-holder height-200"
                    />
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default text-center">
                  <div className="panel-body">
                    <h4 className="text-headline">Overall Performance</h4>
                    <p className="text-h2 text-primary">
                      <strong>+309</strong>
                    </p>
                  </div>
                  <hr className="margin-none" />
                  <div className="panel-body">
                    <div className="sparkline-bar" sparkheight="66">
                      <span className="hide">
                        0:10,7:3,5:5,6:4,3:7,7:3,5:5,6:4,2:8,3:7,7:3,5:5,0:10
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="media sparkline-stats">
                    <div className="pull-left">
                      <div className="panel-body">
                        <div className="sparkline-bar" data-colors="true">
                          <span>6,5,8,6,1</span>
                        </div>
                      </div>
                    </div>
                    <div className="media-body">
                      <ul className="list-group">
                        <li className="list-group-item">
                          <i className="fa fa-fw fa-square text-primary" />
                          <strong>5,931</strong> Visits
                        </li>
                        <li className="list-group-item">
                          <i className="fa fa-fw fa-square text-success" />
                          <strong>402</strong> Conversions
                        </li>
                        <li className="list-group-item">
                          <i className="fa fa-fw fa-square text-danger" />
                          <strong>402</strong> Conversions
                        </li>
                        <li className="list-group-item">
                          <i className="fa fa-fw fa-square text-muted" />
                          <strong>15,120</strong> Pageviews
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>

              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div
                      id="chart_horizontal_bars"
                      data-toggle="flot-chart-horizontal-bars"
                      className="flotchart-holder height-200"
                    />
                  </div>
                </div>
              </div>
            </div>

            <h1 className="text-headline page-section-heading">User</h1>
            <div className="row" data-toggle="isotope">
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default">
                  <div className="profile-block">
                    <div className="cover overlay cover-image-full">
                      <img src="images/place1-full.jpg" alt="cover" />
                      <div className="overlay overlay-full overlay-bg-black">
                        <div className="v-top v-spacing-2">
                          <a href="#" className="icon pull-right">
                            <i className="fa fa-comment" />
                          </a>
                          <div className="text-headline text-overlay">
                            Adrian Demian
                          </div>
                          <p className="text-overlay">
                            User Interface Designer
                          </p>
                        </div>
                        <div className="v-bottom">
                          <a href="#">
                            <img
                              src="images/people/110/guy-6.jpg"
                              alt="people"
                              className="img-circle avatar"
                            />
                          </a>
                        </div>
                      </div>
                    </div>

                    <div className="profile-icons">
                      <span>
                        <i className="fa fa-users" /> 372
                      </span>{" "}
                      <span>
                        <i className="fa fa-photo" /> 43
                      </span>
                      <span>
                        <i className="fa fa-video-camera" /> 3{" "}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default widget-user-1 text-center">
                  <div className="avatar">
                    <img
                      src="images/people/110/guy-5.jpg"
                      alt=""
                      className="img-circle"
                    />
                    <h3>Adrian Demian</h3>
                    <a href="#" className="btn btn-success">
                      Following <i className="fa fa-check-circle fa-fw" />
                    </a>
                  </div>
                  <div className="profile-icons margin-none">
                    <span>
                      <i className="fa fa-users" /> 372
                    </span>
                    <span>
                      <i className="fa fa-photo" /> 43
                    </span>
                    <span>
                      <i className="fa fa-video-camera" /> 3
                    </span>
                  </div>
                  <div className="panel-body">
                    <div className="expandable expandable-indicator-white expandable-trigger">
                      <div className="expandable-content">
                        <p>
                          Hi! Im Adrian the Senior UI Designer at
                          <strong>MOSAICPRO</strong>. We hope you enjoy the
                          design and quality of Social.
                        </p>
                        <p>
                          Lorem ipsum dolor sit amet, consectetur adipisicing
                          elit. Aut autem delectus dolorum necessitatibus neque
                          odio quam quas qui quod soluta? Aliquid eius esse
                          minima.
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-heading">
                    <div className="media">
                      <div className="pull-left">
                        <img
                          src="images/people/50/guy-8.jpg"
                          alt="people"
                          className="media-object img-circle"
                        />
                      </div>
                      <div className="media-body">
                        <h4 className="media-heading margin-v-5">
                          <a href="#">Adrian D.</a>
                        </h4>
                        <div className="profile-icons">
                          <span>
                            <i className="fa fa-users" /> 372
                          </span>
                          <span>
                            <i className="fa fa-photo" /> 43
                          </span>
                          <span>
                            <i className="fa fa-video-camera" /> 3
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="panel-body">
                    <p className="common-friends">Common Friends</p>
                    <div className="user-friend-list">
                      <a href="#">
                        <img
                          src="images/people/50/guy-9.jpg"
                          alt="people"
                          className="img-circle"
                        />
                      </a>
                      <a href="#">
                        <img
                          src="images/people/50/guy-3.jpg"
                          alt="people"
                          className="img-circle"
                        />
                      </a>
                      <a href="#">
                        <img
                          src="images/people/50/woman-7.jpg"
                          alt="people"
                          className="img-circle"
                        />
                      </a>
                      <a href="#">
                        <img
                          src="images/people/50/woman-1.jpg"
                          alt="people"
                          className="img-circle"
                        />
                      </a>
                    </div>
                  </div>
                  <div className="panel-footer">
                    <a href="#" className="btn btn-default btn-sm">
                      Follow <i className="fa fa-share" />
                    </a>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-heading panel-heading-gray">
                    <div className="pull-right">
                      <a href="#" className="btn btn-primary btn-xs">
                        Add <i className="fa fa-plus" />
                      </a>
                    </div>
                    <i className="icon-user-1" /> Friends
                  </div>
                  <div className="panel-body">
                    <ul className="img-grid">
                      <li>
                        <a href="#">
                          <img src="images/people/110/guy-6.jpg" alt="image" />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img
                            src="images/people/110/woman-3.jpg"
                            alt="image"
                          />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img src="images/people/110/guy-2.jpg" alt="image" />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img src="images/people/110/guy-9.jpg" alt="image" />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img
                            src="images/people/110/woman-9.jpg"
                            alt="image"
                          />
                        </a>
                      </li>
                      <li className="clearfix">
                        <a href="#">
                          <img src="images/people/110/guy-4.jpg" alt="image" />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img src="images/people/110/guy-1.jpg" alt="image" />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img
                            src="images/people/110/woman-4.jpg"
                            alt="image"
                          />
                        </a>
                      </li>
                      <li>
                        <a href="#">
                          <img src="images/people/110/guy-6.jpg" alt="image" />
                        </a>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="media user-box">
                      <a className="media-left" href="#">
                        <img
                          src="images/people/50/guy-5.jpg"
                          className="media-object img-circle"
                          alt="Avatar Photo"
                        />
                      </a>
                      <div className="media-body">
                        <h4 className="media-heading margin-v-5">
                          Jonathan Smith
                        </h4>
                        <p className="text-uppercase text-muted">
                          Works at Mosaicpro
                        </p>
                      </div>

                      <div className="media-right">
                        <a className="btn btn-primary btn-sm" href="#">
                          <i className="fa fa-envelope" />
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <h1 className="text-headline page-section-heading">Weather</h1>

            <div className="row" data-toggle="isotope">
              <div className="item col-lg-4 col-md-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="weather-svg">
                    <div className="list">
                      <div className="list-item">
                        <div className="text-h6">Today</div>
                        <svg
                          version="1.1"
                          id="cloudDrizzleSunFill"
                          className="climacon climacon_cloudDrizzleSunFill"
                          xmlns="http://www.w3.org/2000/svg"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          x="0px"
                          y="0px"
                          viewBox="15 15 70 70"
                          enableBackground="new 15 15 70 70"
                          xmlSpace="preserve"
                        >
                          <g className="climacon_iconWrap climacon_iconWrap-cloudDrizzleSunFill">
                            <g className="climacon_componentWrap climacon_componentWrap-sun climacon_componentWrap-sun_cloud">
                              <g className="climacon_componentWrap climacon_componentWrap_sunSpoke">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M80.029,43.611h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S81.135,43.611,80.029,43.611z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M72.174,30.3c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L72.174,30.3z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M58.033,25.614c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C60.033,24.718,59.135,25.614,58.033,25.614z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M43.892,30.3l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C45.939,31.081,44.673,31.081,43.892,30.3z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M42.033,41.612c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C41.139,39.612,42.033,40.509,42.033,41.612z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M43.892,52.925c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L43.892,52.925z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M58.033,57.61c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C56.033,58.505,56.928,57.61,58.033,57.61z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M72.174,52.925l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C70.125,52.144,71.391,52.144,72.174,52.925z"
                                />
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap-sunBody">
                                <circle
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                  cx="58.033"
                                  cy="41.612"
                                  r="11.999"
                                />
                                <circle
                                  className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                  fill="#FFFFFF"
                                  cx="58.033"
                                  cy="41.612"
                                  r="7.999"
                                />
                              </g>
                            </g>
                            <g className="climacon_componentWrap climacon_componentWrap-drizzle">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-left"
                                d="M42.001,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2.001-0.895-2.001-2v-3.998C40,54.538,40.896,53.644,42.001,53.644z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-middle"
                                d="M49.999,53.644c1.104,0,2,0.896,2,2v4c0,1.104-0.896,2-2,2s-1.998-0.896-1.998-2v-4C48.001,54.54,48.896,53.644,49.999,53.644z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-right"
                                d="M57.999,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2-0.895-2-2v-3.998C55.999,54.538,56.894,53.644,57.999,53.644z"
                              />
                            </g>

                            <g className="climacon_componentWrap climacon_componentWrap_cloud">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                              />
                              <path
                                className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                fill="#FFFFFF"
                                d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                              />
                            </g>
                          </g>
                        </svg>
                      </div>
                      <div className="list-item">
                        <div className="text-h6">Tomorrow</div>
                        <svg
                          version="1.1"
                          id="sun"
                          className="climacon climacon_sun"
                          xmlns="http://www.w3.org/2000/svg"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          x="0px"
                          y="0px"
                          viewBox="15 15 70 70"
                          enableBackground="new 15 15 70 70"
                          xmlSpace="preserve"
                        >
                          <clipPath id="svgs/sunFillClip">
                            <path d="M0,0v100h100V0H0z M50.001,57.999c-4.417,0-8-3.582-8-7.999c0-4.418,3.582-7.999,8-7.999s7.998,3.581,7.998,7.999C57.999,54.417,54.418,57.999,50.001,57.999z" />
                          </clipPath>
                          <g className="climacon_iconWrap climacon_iconWrap-sun">
                            <g className="climacon_componentWrap climacon_componentWrap-sun">
                              <g className="climacon_componentWrap climacon_componentWrap-sunSpoke">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-east"
                                  d="M72.03,51.999h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S73.136,51.999,72.03,51.999z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northEast"
                                  d="M64.175,38.688c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L64.175,38.688z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M50.034,34.002c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C52.034,33.106,51.136,34.002,50.034,34.002z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northWest"
                                  d="M35.893,38.688l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C37.94,39.469,36.674,39.469,35.893,38.688z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-west"
                                  d="M34.034,50c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C33.14,48,34.034,48.896,34.034,50z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southWest"
                                  d="M35.893,61.312c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L35.893,61.312z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-south"
                                  d="M50.034,65.998c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C48.034,66.893,48.929,65.998,50.034,65.998z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southEast"
                                  d="M64.175,61.312l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C62.126,60.531,63.392,60.531,64.175,61.312z"
                                />
                              </g>
                              <g
                                className="climacon_componentWrap climacon_componentWrap_sunBody"
                                clipPath="url(#sunFillClip)"
                              >
                                <circle
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                  cx="50.034"
                                  cy="50"
                                  r="11.999"
                                />
                              </g>
                            </g>
                          </g>
                        </svg>
                      </div>
                      <div className="list-item">
                        <div className="text-h6">Saturday</div>

                        <svg
                          version="1.1"
                          id="cloudRainFill"
                          className="climacon climacon_cloudRainFill"
                          xmlns="http://www.w3.org/2000/svg"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          x="0px"
                          y="0px"
                          viewBox="15 15 70 70"
                          enableBackground="new 15 15 70 70"
                          xmlSpace="preserve"
                        >
                          <g className="climacon_iconWrap climacon_iconWrap-cloudRainFill">
                            <g className="climacon_componentWrap climacon_componentWrap-rain">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                              />
                            </g>
                            <g className="climacon_componentWrap climacon_componentWrap_cloud">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                              />
                              <path
                                className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                fill="#FFFFFF"
                                d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                              />
                            </g>
                          </g>
                        </svg>
                      </div>
                    </div>
                    <hr className="margin-none" />
                    <div className="today text-center">
                      <svg
                        version="1.1"
                        id="cloudDrizzleSunFill"
                        className="climacon climacon_cloudDrizzleSunFill"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlnsXlink="http://www.w3.org/1999/xlink"
                        x="0px"
                        y="0px"
                        viewBox="15 15 70 70"
                        enableBackground="new 15 15 70 70"
                        xmlSpace="preserve"
                      >
                        <g className="climacon_iconWrap climacon_iconWrap-cloudDrizzleSunFill">
                          <g className="climacon_componentWrap climacon_componentWrap-sun climacon_componentWrap-sun_cloud">
                            <g className="climacon_componentWrap climacon_componentWrap_sunSpoke">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M80.029,43.611h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S81.135,43.611,80.029,43.611z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M72.174,30.3c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L72.174,30.3z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M58.033,25.614c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C60.033,24.718,59.135,25.614,58.033,25.614z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M43.892,30.3l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C45.939,31.081,44.673,31.081,43.892,30.3z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M42.033,41.612c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C41.139,39.612,42.033,40.509,42.033,41.612z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M43.892,52.925c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L43.892,52.925z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M58.033,57.61c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C56.033,58.505,56.928,57.61,58.033,57.61z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M72.174,52.925l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C70.125,52.144,71.391,52.144,72.174,52.925z"
                              />
                            </g>
                            <g className="climacon_componentWrap climacon_componentWrap-sunBody">
                              <circle
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                cx="58.033"
                                cy="41.612"
                                r="11.999"
                              />
                              <circle
                                className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                fill="#FFFFFF"
                                cx="58.033"
                                cy="41.612"
                                r="7.999"
                              />
                            </g>
                          </g>
                          <g className="climacon_componentWrap climacon_componentWrap-drizzle">
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-left"
                              d="M42.001,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2.001-0.895-2.001-2v-3.998C40,54.538,40.896,53.644,42.001,53.644z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-middle"
                              d="M49.999,53.644c1.104,0,2,0.896,2,2v4c0,1.104-0.896,2-2,2s-1.998-0.896-1.998-2v-4C48.001,54.54,48.896,53.644,49.999,53.644z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-right"
                              d="M57.999,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2-0.895-2-2v-3.998C55.999,54.538,56.894,53.644,57.999,53.644z"
                            />
                          </g>

                          <g className="climacon_componentWrap climacon_componentWrap_cloud">
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                              d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                            />
                            <path
                              className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                              fill="#FFFFFF"
                              d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                            />
                          </g>
                        </g>
                      </svg>

                      <div className="clearfix" />
                      <div className="details">
                        Today
                        <strong className="pull-right"> 10&deg; C </strong>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-lg-4 col-md-6 col-xs-12">
                <div className="panel panel-default weather-svg">
                  <div className="list">
                    <div className="list-item">
                      <div className="text-h6">Today</div>
                      <svg
                        version="1.1"
                        id="cloudDrizzleSunFill"
                        className="climacon climacon_cloudDrizzleSunFill"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlnsXlink="http://www.w3.org/1999/xlink"
                        x="0px"
                        y="0px"
                        viewBox="15 15 70 70"
                        enableBackground="new 15 15 70 70"
                        xmlSpace="preserve"
                      >
                        <g className="climacon_iconWrap climacon_iconWrap-cloudDrizzleSunFill">
                          <g className="climacon_componentWrap climacon_componentWrap-sun climacon_componentWrap-sun_cloud">
                            <g className="climacon_componentWrap climacon_componentWrap_sunSpoke">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M80.029,43.611h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S81.135,43.611,80.029,43.611z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M72.174,30.3c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L72.174,30.3z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M58.033,25.614c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C60.033,24.718,59.135,25.614,58.033,25.614z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M43.892,30.3l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C45.939,31.081,44.673,31.081,43.892,30.3z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M42.033,41.612c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C41.139,39.612,42.033,40.509,42.033,41.612z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M43.892,52.925c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L43.892,52.925z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M58.033,57.61c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C56.033,58.505,56.928,57.61,58.033,57.61z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M72.174,52.925l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C70.125,52.144,71.391,52.144,72.174,52.925z"
                              />
                            </g>
                            <g className="climacon_componentWrap climacon_componentWrap-sunBody">
                              <circle
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                cx="58.033"
                                cy="41.612"
                                r="11.999"
                              />
                              <circle
                                className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                fill="#FFFFFF"
                                cx="58.033"
                                cy="41.612"
                                r="7.999"
                              />
                            </g>
                          </g>
                          <g className="climacon_componentWrap climacon_componentWrap-drizzle">
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-left"
                              d="M42.001,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2.001-0.895-2.001-2v-3.998C40,54.538,40.896,53.644,42.001,53.644z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-middle"
                              d="M49.999,53.644c1.104,0,2,0.896,2,2v4c0,1.104-0.896,2-2,2s-1.998-0.896-1.998-2v-4C48.001,54.54,48.896,53.644,49.999,53.644z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-right"
                              d="M57.999,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2-0.895-2-2v-3.998C55.999,54.538,56.894,53.644,57.999,53.644z"
                            />
                          </g>

                          <g className="climacon_componentWrap climacon_componentWrap_cloud">
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                              d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                            />
                            <path
                              className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                              fill="#FFFFFF"
                              d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                            />
                          </g>
                        </g>
                      </svg>
                    </div>
                    <div className="list-item">
                      <div className="text-h6">Tomorrow</div>
                      <svg
                        version="1.1"
                        id="sun"
                        className="climacon climacon_sun"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlnsXlink="http://www.w3.org/1999/xlink"
                        x="0px"
                        y="0px"
                        viewBox="15 15 70 70"
                        enableBackground="new 15 15 70 70"
                        xmlSpace="preserve"
                      >
                        <clipPath id="svgs/sunFillClip">
                          <path d="M0,0v100h100V0H0z M50.001,57.999c-4.417,0-8-3.582-8-7.999c0-4.418,3.582-7.999,8-7.999s7.998,3.581,7.998,7.999C57.999,54.417,54.418,57.999,50.001,57.999z" />
                        </clipPath>
                        <g className="climacon_iconWrap climacon_iconWrap-sun">
                          <g className="climacon_componentWrap climacon_componentWrap-sun">
                            <g className="climacon_componentWrap climacon_componentWrap-sunSpoke">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-east"
                                d="M72.03,51.999h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S73.136,51.999,72.03,51.999z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northEast"
                                d="M64.175,38.688c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L64.175,38.688z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                d="M50.034,34.002c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C52.034,33.106,51.136,34.002,50.034,34.002z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northWest"
                                d="M35.893,38.688l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C37.94,39.469,36.674,39.469,35.893,38.688z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-west"
                                d="M34.034,50c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C33.14,48,34.034,48.896,34.034,50z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southWest"
                                d="M35.893,61.312c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L35.893,61.312z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-south"
                                d="M50.034,65.998c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C48.034,66.893,48.929,65.998,50.034,65.998z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southEast"
                                d="M64.175,61.312l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C62.126,60.531,63.392,60.531,64.175,61.312z"
                              />
                            </g>
                            <g
                              className="climacon_componentWrap climacon_componentWrap_sunBody"
                              clipPath="url(#sunFillClip)"
                            >
                              <circle
                                className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                cx="50.034"
                                cy="50"
                                r="11.999"
                              />
                            </g>
                          </g>
                        </g>
                      </svg>
                    </div>
                    <div className="list-item">
                      <div className="text-h6">Saturday</div>

                      <svg
                        version="1.1"
                        id="cloudRainFill"
                        className="climacon climacon_cloudRainFill"
                        xmlns="http://www.w3.org/2000/svg"
                        xmlnsXlink="http://www.w3.org/1999/xlink"
                        x="0px"
                        y="0px"
                        viewBox="15 15 70 70"
                        enableBackground="new 15 15 70 70"
                        xmlSpace="preserve"
                      >
                        <g className="climacon_iconWrap climacon_iconWrap-cloudRainFill">
                          <g className="climacon_componentWrap climacon_componentWrap-rain">
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                              d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                              d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                              d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                              d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                              d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                            />
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                              d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                            />
                          </g>
                          <g className="climacon_componentWrap climacon_componentWrap_cloud">
                            <path
                              className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                              d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                            />
                            <path
                              className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                              fill="#FFFFFF"
                              d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                            />
                          </g>
                        </g>
                      </svg>
                    </div>
                  </div>
                  <div className="cover overlay cover-image-full">
                    <img src="images/place1-full.jpg" alt="cover" />
                    <div className="overlay overlay-full overlay-bg-black">
                      <div className="v-top">
                        <div className="text-h5">
                          <span className="fa fa-fw fa-star text-primary" />
                          <span className="fa fa-fw fa-star text-primary" />
                          <span className="fa fa-fw fa-star text-primary" />
                          <span className="fa fa-fw fa-star-o text-white" />
                          <span className="fa fa-fw fa-star-o text-white" />
                        </div>
                      </div>
                      <div className="v-center today">
                        <svg
                          version="1.1"
                          id="cloudDrizzleSunFillAlt"
                          className="climacon climacon_cloudDrizzleSunFillAlt"
                          xmlns="http://www.w3.org/2000/svg"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          x="0px"
                          y="0px"
                          viewBox="15 15 70 70"
                          enableBackground="new 15 15 70 70"
                          xmlSpace="preserve"
                        >
                          <g className="climacon_iconWrap climacon_iconWrap-cloudDrizzleSunFillAlt">
                            <g className="climacon_componentWrap climacon_componentWrap-sun climacon_componentWrap-sun_cloud">
                              <g className="climacon_componentWrap climacon_componentWrap_sunSpoke">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M80.029,43.611h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S81.135,43.611,80.029,43.611z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M72.174,30.3c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L72.174,30.3z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M58.033,25.614c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C60.033,24.718,59.135,25.614,58.033,25.614z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M43.892,30.3l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C45.939,31.081,44.673,31.081,43.892,30.3z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M42.033,41.612c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C41.139,39.612,42.033,40.509,42.033,41.612z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M43.892,52.925c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L43.892,52.925z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M58.033,57.61c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C56.033,58.505,56.928,57.61,58.033,57.61z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M72.174,52.925l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C70.125,52.144,71.391,52.144,72.174,52.925z"
                                />
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap-sunBody">
                                <circle
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                  cx="58.033"
                                  cy="41.612"
                                  r="11.999"
                                />
                                <circle
                                  className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                  fill="#FFFFFF"
                                  cx="58.033"
                                  cy="41.612"
                                  r="7.999"
                                />
                              </g>
                            </g>
                            <g className="climacon_componentWrap climacon_componentWrap-drizzle">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-left"
                                id="Drizzle-Left_1_"
                                d="M56.969,57.672l-2.121,2.121c-1.172,1.172-1.172,3.072,0,4.242c1.17,1.172,3.07,1.172,4.24,0c1.172-1.17,1.172-3.07,0-4.242L56.969,57.672z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-middle"
                                d="M50.088,57.672l-2.119,2.121c-1.174,1.172-1.174,3.07,0,4.242c1.17,1.172,3.068,1.172,4.24,0s1.172-3.07,0-4.242L50.088,57.672z"
                              />
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-right"
                                d="M43.033,57.672l-2.121,2.121c-1.172,1.172-1.172,3.07,0,4.242s3.07,1.172,4.244,0c1.172-1.172,1.172-3.07,0-4.242L43.033,57.672z"
                              />
                            </g>
                            <g className="climacon_componentWrap climacon_componentWrap_cloud">
                              <path
                                className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                              />
                              <path
                                className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                fill="#FFFFFF"
                                d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                              />
                            </g>
                          </g>
                        </svg>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-lg-4 col-md-6 col-xs-12">
                <div className="panel panel-default weather-svg">
                  <div className="cover overlay cover-image-full">
                    <img src="images/place2-full.jpg" alt="cover" />
                    <div className="overlay overlay-full overlay-bg-black">
                      <div className="v-top">
                        <div className="pull-right">
                          <a href="#">
                            <i className="fa fa-cog" />
                          </a>
                        </div>
                        <a href="#">Miami, Florida, US</a>
                        <p className="small">24&deg; C - Sunny</p>
                      </div>
                      <div className="v-center today">
                        <svg
                          version="1.1"
                          id="sunFill"
                          className="climacon climacon_sunFill"
                          xmlns="http://www.w3.org/2000/svg"
                          xmlnsXlink="http://www.w3.org/1999/xlink"
                          x="0px"
                          y="0px"
                          viewBox="15 15 70 70"
                          enableBackground="new 15 15 70 70"
                          xmlSpace="preserve"
                        >
                          <g className="climacon_iconWrap climacon_iconWrap-sunFill">
                            <g className="climacon_componentWrap climacon_componentWrap-sun">
                              <g className="climacon_componentWrap climacon_componentWrap-sunSpoke">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-east"
                                  d="M72.03,51.999h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S73.136,51.999,72.03,51.999z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northEast"
                                  d="M64.175,38.688c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L64.175,38.688z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                  d="M50.034,34.002c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C52.034,33.106,51.136,34.002,50.034,34.002z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northWest"
                                  d="M35.893,38.688l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C37.94,39.469,36.674,39.469,35.893,38.688z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-west"
                                  d="M34.034,50c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C33.14,48,34.034,48.896,34.034,50z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southWest"
                                  d="M35.893,61.312c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L35.893,61.312z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-south"
                                  d="M50.034,65.998c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C48.034,66.893,48.929,65.998,50.034,65.998z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southEast"
                                  d="M64.175,61.312l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C62.126,60.531,63.392,60.531,64.175,61.312z"
                                />
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap_sunBody">
                                <circle
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                  cx="50.034"
                                  cy="50"
                                  r="11.999"
                                />
                                <circle
                                  className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                  fill="#FFFFFF"
                                  cx="50.034"
                                  cy="50"
                                  r="7.999"
                                />
                              </g>
                            </g>
                          </g>
                        </svg>
                      </div>
                    </div>
                    <div className="overlay overlay-bg-black padding-none">
                      <div className="list">
                        <div className="list-item">
                          <div className="text-h6">Mon</div>
                          <svg
                            version="1.1"
                            id="cloudDrizzleSunFill"
                            className="climacon climacon_cloudDrizzleSunFill"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <g className="climacon_iconWrap climacon_iconWrap-cloudDrizzleSunFill">
                              <g className="climacon_componentWrap climacon_componentWrap-sun climacon_componentWrap-sun_cloud">
                                <g className="climacon_componentWrap climacon_componentWrap_sunSpoke">
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M80.029,43.611h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S81.135,43.611,80.029,43.611z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M72.174,30.3c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L72.174,30.3z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M58.033,25.614c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C60.033,24.718,59.135,25.614,58.033,25.614z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M43.892,30.3l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C45.939,31.081,44.673,31.081,43.892,30.3z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M42.033,41.612c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C41.139,39.612,42.033,40.509,42.033,41.612z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M43.892,52.925c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L43.892,52.925z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M58.033,57.61c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C56.033,58.505,56.928,57.61,58.033,57.61z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M72.174,52.925l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C70.125,52.144,71.391,52.144,72.174,52.925z"
                                  />
                                </g>
                                <g className="climacon_componentWrap climacon_componentWrap-sunBody">
                                  <circle
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                    cx="58.033"
                                    cy="41.612"
                                    r="11.999"
                                  />
                                  <circle
                                    className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                    fill="#FFFFFF"
                                    cx="58.033"
                                    cy="41.612"
                                    r="7.999"
                                  />
                                </g>
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap-drizzle">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-left"
                                  d="M42.001,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2.001-0.895-2.001-2v-3.998C40,54.538,40.896,53.644,42.001,53.644z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-middle"
                                  d="M49.999,53.644c1.104,0,2,0.896,2,2v4c0,1.104-0.896,2-2,2s-1.998-0.896-1.998-2v-4C48.001,54.54,48.896,53.644,49.999,53.644z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-right"
                                  d="M57.999,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2-0.895-2-2v-3.998C55.999,54.538,56.894,53.644,57.999,53.644z"
                                />
                              </g>

                              <g className="climacon_componentWrap climacon_componentWrap_cloud">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                  d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                                />
                                <path
                                  className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                  fill="#FFFFFF"
                                  d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                                />
                              </g>
                            </g>
                          </svg>
                        </div>
                        <div className="list-item">
                          <div className="text-h6">Tue</div>
                          <svg
                            version="1.1"
                            id="sun"
                            className="climacon climacon_sun"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <clipPath id="svgs/sunFillClip">
                              <path d="M0,0v100h100V0H0z M50.001,57.999c-4.417,0-8-3.582-8-7.999c0-4.418,3.582-7.999,8-7.999s7.998,3.581,7.998,7.999C57.999,54.417,54.418,57.999,50.001,57.999z" />
                            </clipPath>
                            <g className="climacon_iconWrap climacon_iconWrap-sun">
                              <g className="climacon_componentWrap climacon_componentWrap-sun">
                                <g className="climacon_componentWrap climacon_componentWrap-sunSpoke">
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-east"
                                    d="M72.03,51.999h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S73.136,51.999,72.03,51.999z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northEast"
                                    d="M64.175,38.688c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L64.175,38.688z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M50.034,34.002c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C52.034,33.106,51.136,34.002,50.034,34.002z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northWest"
                                    d="M35.893,38.688l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C37.94,39.469,36.674,39.469,35.893,38.688z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-west"
                                    d="M34.034,50c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C33.14,48,34.034,48.896,34.034,50z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southWest"
                                    d="M35.893,61.312c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L35.893,61.312z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-south"
                                    d="M50.034,65.998c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C48.034,66.893,48.929,65.998,50.034,65.998z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southEast"
                                    d="M64.175,61.312l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C62.126,60.531,63.392,60.531,64.175,61.312z"
                                  />
                                </g>
                                <g
                                  className="climacon_componentWrap climacon_componentWrap_sunBody"
                                  clipPath="url(#sunFillClip)"
                                >
                                  <circle
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                    cx="50.034"
                                    cy="50"
                                    r="11.999"
                                  />
                                </g>
                              </g>
                            </g>
                          </svg>
                        </div>
                        <div className="list-item">
                          <div className="text-h6">Wed</div>

                          <svg
                            version="1.1"
                            id="cloudRainFill"
                            className="climacon climacon_cloudRainFill"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <g className="climacon_iconWrap climacon_iconWrap-cloudRainFill">
                              <g className="climacon_componentWrap climacon_componentWrap-rain">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                  d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                  d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                  d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                  d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                  d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                  d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                                />
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap_cloud">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                  d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                                />
                                <path
                                  className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                  fill="#FFFFFF"
                                  d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                                />
                              </g>
                            </g>
                          </svg>
                        </div>
                        <div className="list-item">
                          <div className="text-h6">Thu</div>
                          <svg
                            version="1.1"
                            id="cloudDrizzleSunFill"
                            className="climacon climacon_cloudDrizzleSunFill"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <g className="climacon_iconWrap climacon_iconWrap-cloudDrizzleSunFill">
                              <g className="climacon_componentWrap climacon_componentWrap-sun climacon_componentWrap-sun_cloud">
                                <g className="climacon_componentWrap climacon_componentWrap_sunSpoke">
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M80.029,43.611h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S81.135,43.611,80.029,43.611z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M72.174,30.3c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L72.174,30.3z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M58.033,25.614c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C60.033,24.718,59.135,25.614,58.033,25.614z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M43.892,30.3l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C45.939,31.081,44.673,31.081,43.892,30.3z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M42.033,41.612c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C41.139,39.612,42.033,40.509,42.033,41.612z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M43.892,52.925c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L43.892,52.925z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M58.033,57.61c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C56.033,58.505,56.928,57.61,58.033,57.61z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M72.174,52.925l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C70.125,52.144,71.391,52.144,72.174,52.925z"
                                  />
                                </g>
                                <g className="climacon_componentWrap climacon_componentWrap-sunBody">
                                  <circle
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                    cx="58.033"
                                    cy="41.612"
                                    r="11.999"
                                  />
                                  <circle
                                    className="climacon_component climacon_component-fill climacon_component-fill_sunBody"
                                    fill="#FFFFFF"
                                    cx="58.033"
                                    cy="41.612"
                                    r="7.999"
                                  />
                                </g>
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap-drizzle">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-left"
                                  d="M42.001,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2.001-0.895-2.001-2v-3.998C40,54.538,40.896,53.644,42.001,53.644z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-middle"
                                  d="M49.999,53.644c1.104,0,2,0.896,2,2v4c0,1.104-0.896,2-2,2s-1.998-0.896-1.998-2v-4C48.001,54.54,48.896,53.644,49.999,53.644z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_drizzle climacon_component-stroke_drizzle-right"
                                  d="M57.999,53.644c1.104,0,2,0.896,2,2v3.998c0,1.105-0.896,2-2,2c-1.105,0-2-0.895-2-2v-3.998C55.999,54.538,56.894,53.644,57.999,53.644z"
                                />
                              </g>

                              <g className="climacon_componentWrap climacon_componentWrap_cloud">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                  d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                                />
                                <path
                                  className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                  fill="#FFFFFF"
                                  d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                                />
                              </g>
                            </g>
                          </svg>
                        </div>
                        <div className="list-item">
                          <div className="text-h6">Fri</div>
                          <svg
                            version="1.1"
                            id="sun"
                            className="climacon climacon_sun"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <clipPath id="svgs/sunFillClip">
                              <path d="M0,0v100h100V0H0z M50.001,57.999c-4.417,0-8-3.582-8-7.999c0-4.418,3.582-7.999,8-7.999s7.998,3.581,7.998,7.999C57.999,54.417,54.418,57.999,50.001,57.999z" />
                            </clipPath>
                            <g className="climacon_iconWrap climacon_iconWrap-sun">
                              <g className="climacon_componentWrap climacon_componentWrap-sun">
                                <g className="climacon_componentWrap climacon_componentWrap-sunSpoke">
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-east"
                                    d="M72.03,51.999h-3.998c-1.105,0-2-0.896-2-1.999s0.895-2,2-2h3.998c1.104,0,2,0.896,2,2S73.136,51.999,72.03,51.999z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northEast"
                                    d="M64.175,38.688c-0.781,0.781-2.049,0.781-2.828,0c-0.781-0.781-0.781-2.047,0-2.828l2.828-2.828c0.779-0.781,2.047-0.781,2.828,0c0.779,0.781,0.779,2.047,0,2.828L64.175,38.688z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-north"
                                    d="M50.034,34.002c-1.105,0-2-0.896-2-2v-3.999c0-1.104,0.895-2,2-2c1.104,0,2,0.896,2,2v3.999C52.034,33.106,51.136,34.002,50.034,34.002z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-northWest"
                                    d="M35.893,38.688l-2.827-2.828c-0.781-0.781-0.781-2.047,0-2.828c0.78-0.781,2.047-0.781,2.827,0l2.827,2.828c0.781,0.781,0.781,2.047,0,2.828C37.94,39.469,36.674,39.469,35.893,38.688z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-west"
                                    d="M34.034,50c0,1.104-0.896,1.999-2,1.999h-4c-1.104,0-1.998-0.896-1.998-1.999s0.896-2,1.998-2h4C33.14,48,34.034,48.896,34.034,50z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southWest"
                                    d="M35.893,61.312c0.781-0.78,2.048-0.78,2.827,0c0.781,0.78,0.781,2.047,0,2.828l-2.827,2.827c-0.78,0.781-2.047,0.781-2.827,0c-0.781-0.78-0.781-2.047,0-2.827L35.893,61.312z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-south"
                                    d="M50.034,65.998c1.104,0,2,0.895,2,1.999v4c0,1.104-0.896,2-2,2c-1.105,0-2-0.896-2-2v-4C48.034,66.893,48.929,65.998,50.034,65.998z"
                                  />
                                  <path
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunSpoke climacon_component-stroke_sunSpoke-southEast"
                                    d="M64.175,61.312l2.828,2.828c0.779,0.78,0.779,2.047,0,2.827c-0.781,0.781-2.049,0.781-2.828,0l-2.828-2.827c-0.781-0.781-0.781-2.048,0-2.828C62.126,60.531,63.392,60.531,64.175,61.312z"
                                  />
                                </g>
                                <g
                                  className="climacon_componentWrap climacon_componentWrap_sunBody"
                                  clipPath="url(#sunFillClip)"
                                >
                                  <circle
                                    className="climacon_component climacon_component-stroke climacon_component-stroke_sunBody"
                                    cx="50.034"
                                    cy="50"
                                    r="11.999"
                                  />
                                </g>
                              </g>
                            </g>
                          </svg>
                        </div>
                        <div className="list-item">
                          <div className="text-h6">Sat</div>

                          <svg
                            version="1.1"
                            id="cloudRainFill"
                            className="climacon climacon_cloudRainFill"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <g className="climacon_iconWrap climacon_iconWrap-cloudRainFill">
                              <g className="climacon_componentWrap climacon_componentWrap-rain">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                  d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                  d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                  d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                  d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                  d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                  d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                                />
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap_cloud">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                  d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                                />
                                <path
                                  className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                  fill="#FFFFFF"
                                  d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                                />
                              </g>
                            </g>
                          </svg>
                        </div>
                        <div className="list-item">
                          <div className="text-h6">Sun</div>

                          <svg
                            version="1.1"
                            id="cloudRainFill"
                            className="climacon climacon_cloudRainFill"
                            xmlns="http://www.w3.org/2000/svg"
                            xmlnsXlink="http://www.w3.org/1999/xlink"
                            x="0px"
                            y="0px"
                            viewBox="15 15 70 70"
                            enableBackground="new 15 15 70 70"
                            xmlSpace="preserve"
                          >
                            <g className="climacon_iconWrap climacon_iconWrap-cloudRainFill">
                              <g className="climacon_componentWrap climacon_componentWrap-rain">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                  d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                  d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                  d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- left"
                                  d="M41.946,53.641c1.104,0,1.999,0.896,1.999,2v15.998c0,1.105-0.895,2-1.999,2s-2-0.895-2-2V55.641C39.946,54.537,40.842,53.641,41.946,53.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- middle"
                                  d="M49.945,57.641c1.104,0,2,0.896,2,2v15.998c0,1.104-0.896,2-2,2s-2-0.896-2-2V59.641C47.945,58.535,48.841,57.641,49.945,57.641z"
                                />
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_rain climacon_component-stroke_rain- right"
                                  d="M57.943,53.641c1.104,0,2,0.896,2,2v15.998c0,1.105-0.896,2-2,2c-1.104,0-2-0.895-2-2V55.641C55.943,54.537,56.84,53.641,57.943,53.641z"
                                />
                              </g>
                              <g className="climacon_componentWrap climacon_componentWrap_cloud">
                                <path
                                  className="climacon_component climacon_component-stroke climacon_component-stroke_cloud"
                                  d="M43.945,65.639c-8.835,0-15.998-7.162-15.998-15.998c0-8.836,7.163-15.998,15.998-15.998c6.004,0,11.229,3.312,13.965,8.203c0.664-0.113,1.338-0.205,2.033-0.205c6.627,0,11.998,5.373,11.998,12c0,6.625-5.371,11.998-11.998,11.998C57.168,65.639,47.143,65.639,43.945,65.639z"
                                />
                                <path
                                  className="climacon_component climacon_component-fill climacon_component-fill_cloud"
                                  fill="#FFFFFF"
                                  d="M59.943,61.639c4.418,0,8-3.582,8-7.998c0-4.417-3.582-8-8-8c-1.601,0-3.082,0.481-4.334,1.291c-1.23-5.316-5.973-9.29-11.665-9.29c-6.626,0-11.998,5.372-11.998,11.999c0,6.626,5.372,11.998,11.998,11.998C47.562,61.639,56.924,61.639,59.943,61.639z"
                                />
                              </g>
                            </g>
                          </svg>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <h2 className="page-section-heading">Tables &amp; lists</h2>

            <div className="row" data-toggle="isotope">
              <div className="item col-md-6 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-heading">
                    <h4 className="panel-title">Top 5</h4>
                  </div>
                  <table className="table table-leaderboard margin-none">
                    <tbody>
                      <tr>
                        <td width="20">1</td>
                        <td>
                          <a href="#">
                            <i className="fa fa-flag text-muted" /> Jonathan
                            Smith
                          </a>
                        </td>
                        <td>
                          <span className="pull-right">14,200</span>
                        </td>
                      </tr>
                      <tr>
                        <td width="20">2</td>
                        <td>
                          <a href="#">Michelle S.</a>
                        </td>
                        <td>
                          <span className="pull-right">11,591</span>
                        </td>
                      </tr>
                      <tr>
                        <td width="20">3</td>
                        <td>
                          <a href="#">Anthony Smith</a>
                        </td>
                        <td>
                          <span className="pull-right">10,232</span>
                        </td>
                      </tr>
                      <tr>
                        <td width="20">4</td>
                        <td>
                          <a href="#">First Smith</a>
                        </td>
                        <td>
                          <span className="pull-right">9,002</span>
                        </td>
                      </tr>
                      <tr>
                        <td width="20">5</td>
                        <td>
                          <a href="#">Second Smith</a>
                        </td>
                        <td>
                          <span className="pull-right">8,694</span>
                        </td>
                      </tr>
                    </tbody>
                  </table>

                  <div className="panel-footer padding-none">
                    <div className="row">
                      <div className="col-sm-6">
                        <div className="score-block">
                          <div className="title">Min</div>
                          <div className="score">126</div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <div className="score-block">
                          <div className="title">Max</div>
                          <div className="score">11,421</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-md-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-heading">
                    <div className="pull-right ">
                      <a href="#" className="btn btn-default btn-xs">
                        <i className="fa fa-cog" />
                      </a>
                    </div>
                    <h4 className="panel-title">Notifications</h4>
                  </div>
                  <ul className="notifications-timeline">
                    <li>
                      <span className="date">24 Aug</span>
                      <span className="circle orange" />
                      <span className="content">
                        <a href="#">Jonathan</a> has a due payment of $299
                      </span>
                    </li>
                    <li>
                      <span className="date">13 Aug</span>
                      <span className="circle blue" />
                      <span className="content">
                        <a href="#">Michelle</a> became a PRO Member.
                      </span>
                    </li>
                    <li>
                      <span className="date">10 Aug</span>
                      <span className="circle gray-light" />
                      <span className="content">
                        <a href="#">Jonathan</a> has registered.
                      </span>
                    </li>
                    <li>
                      <span className="date">1 Aug</span>
                      <span className="circle gray" />
                      <span className="content">
                        This is a basic text notification.
                      </span>
                    </li>
                    <li>
                      <span className="date">27 Jul</span>
                      <span className="circle gray-light" />
                      <span className="content">
                        <a href="#">Jonathan</a> has a due payment of $299
                      </span>
                    </li>
                  </ul>
                </div>
              </div>
              <div className="item col-md-6 col-xs-12">
                <div className="panel panel-default">
                  <table className="table table-striped margin-none">
                    <thead>
                      <tr>
                        <th>Box office</th>
                        <th className="text-right">Cash</th>
                        <th className="text-right width-100">Trend</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>
                          <strong className="text-muted">1.</strong>{" "}
                          <a href="#" className="text-primary">
                            Frozen
                          </a>
                        </td>
                        <td className="text-right">&euro;8,718,939</td>
                        <td className="text-right">
                          <div
                            className="sparkline-line width-100"
                            sparkheight="20"
                            sparkwidth="100%"
                            data-data="[ 484, 457, 397, 591, 496, 508, 366, 196 ]"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <strong className="text-muted">2.</strong>{" "}
                          <a href="#" className="text-primary">
                            The Hobbit 2
                          </a>
                        </td>
                        <td className="text-right">&euro;7,800,000</td>
                        <td className="text-right">
                          <div
                            className="sparkline-line width-100"
                            sparkheight="20"
                            sparkwidth="100%"
                            data-data="[ 363, 371, 221, 258, 318, 582, 536, 312 ]"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <strong className="text-muted">3.</strong>{" "}
                          <a href="#" className="text-primary">
                            The Wolf of Wall
                          </a>
                        </td>
                        <td className="text-right">&euro;5,671,036</td>
                        <td className="text-right">
                          <div
                            className="sparkline-line width-100"
                            sparkheight="20"
                            sparkwidth="100%"
                            data-data="[ 315, 568, 323, 517, 520, 368, 311, 284 ]"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <strong className="text-muted">4.</strong>{" "}
                          <a href="#" className="text-primary">
                            Iron Man 3
                          </a>
                        </td>
                        <td className="text-right">&euro;409,013,994</td>
                        <td className="text-right">
                          <div
                            className="sparkline-line width-100"
                            sparkheight="20"
                            sparkwidth="100%"
                            data-data="[ 188, 522, 457, 246, 323, 456, 429, 478 ]"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <strong className="text-muted">5.</strong>{" "}
                          <a href="#" className="text-primary">
                            Catching Fire
                          </a>
                        </td>
                        <td className="text-right">&euro;398,327,026</td>
                        <td className="text-right">
                          <div
                            className="sparkline-line width-100"
                            sparkheight="20"
                            sparkwidth="100%"
                            data-data="[ 366, 589, 556, 312, 361, 523, 281, 558 ]"
                          />
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <strong className="text-muted">6.</strong>{" "}
                          <a href="#" className="text-primary">
                            Despicable Me
                          </a>
                        </td>
                        <td className="text-right">&euro;367,835,345</td>
                        <td className="text-right">
                          <div
                            className="sparkline-line width-100"
                            sparkheight="20"
                            sparkwidth="100%"
                            data-data="[ 318, 586, 529, 298, 109, 436, 512, 184 ]"
                          />
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <div className="item col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-heading">
                    <h4 className="panel-title">Recent Tickets</h4>
                  </div>
                  <ul className="list-group">
                    <li className="list-group-item">
                      <span className="label label-default">#8010</span> &nbsp;
                      <div className="pull-right">
                        <span className="text-muted  text-small">
                          2 hrs ago
                        </span>
                      </div>
                      <a href="#">How can I use UI Kit?</a>
                    </li>
                    <li className="list-group-item">
                      <span className="label label-default">#8010</span> &nbsp;
                      <div className="pull-right">
                        <span className="text-muted  text-small">
                          2 hrs ago
                        </span>
                      </div>
                      <a href="#">How can I use UI Kit?</a>
                    </li>
                    <li className="list-group-item">
                      <span className="label label-default">#8010</span> &nbsp;
                      <div className="pull-right">
                        <span className="text-muted  text-small">
                          2 hrs ago
                        </span>
                      </div>
                      <a href="#">How can I use UI Kit?</a>
                    </li>
                    <li className="list-group-item">
                      <span className="label label-default">#8010</span> &nbsp;
                      <div className="pull-right">
                        <span className="text-muted  text-small">
                          2 hrs ago
                        </span>
                      </div>
                      <a href="#">How can I use UI Kit?</a>
                    </li>
                    <li className="list-group-item text-right">
                      <a className="btn btn-sm btn-danger" href="#">
                        Go to support <i className="fa fa-fw fa-arrow-right" />
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
              <div className="item col-md-6 col-lg-3 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="media-activity">
                      <div className="media">
                        <i className="pull-left fa fa-globe fa-fw" />
                        <div className="media-body">
                          <span className="title">Visited Miami, FL</span>
                          <span className="time">3 hours ago</span>
                        </div>
                      </div>
                      <div className="media">
                        <i className="pull-left fa fa-camera  fa-fw" />
                        <div className="media-body">
                          <span className="title">Added 5 Photos</span>
                          <span className="time">4 hours ago</span>
                        </div>
                      </div>
                      <div className="media">
                        <i className="pull-left fa fa-users  fa-fw" />
                        <div className="media-body">
                          <span className="title">Samantha is your friend</span>
                          <span className="time">2 days ago</span>
                        </div>
                      </div>
                      <div className="media">
                        <i className="pull-left fa fa-bell-o  fa-fw" />
                        <div className="media-body">
                          <span className="title">
                            Bill got engaged with Michelle
                          </span>
                          <span className="time">3 days ago</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <h1 className="text-headline page-section-heading">Media</h1>

            <div className="row" data-toggle="isotope">
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default gallery-grid">
                  <div className="cover overlay hover cover-image-full">
                    <img src="images/place1-full.jpg" alt="cover" />
                    <a
                      href="#showImageModal"
                      data-toggle="modal"
                      className="overlay overlay-full overlay-hover overlay-bg-black"
                    >
                      <span className="v-center">
                        <span className="btn btn-white btn-lg btn-circle">
                          <i className="fa fa-plus" />
                        </span>
                      </span>
                    </a>
                  </div>
                  <div className="panel-body">
                    <div className="expandable expandable-indicator-white expandable-trigger">
                      <div className="expandable-content">
                        <p>
                          Lorem ipsum dolor sit amet, consectetur adipisicing
                          elit. Asperiores dolore expedita facere fuga ipsum
                          laboriosam minima minus molestias quaerat qui
                          repudiandae, similique vitae. Animi fuga minima nemo,
                          nulla officia quam!
                        </p>
                        <p>
                          Lorem ipsum dolor sit amet, consectetur adipisicing
                          elit. Amet aut eligendi ex, explicabo hic impedit
                          maxime necessitatibus nobis porro sapiente sint,
                          vitae. Facere itaque nihil nostrum pariatur quo
                          temporibus unde.
                        </p>
                      </div>
                    </div>
                  </div>
                  <div className="btn-group btn-group-footer btn-group-justified">
                    <a href="#" className="btn">
                      <i className="fa fa-heart" /> Like
                    </a>
                    <a href="#" className="btn">
                      <i className="fa fa-user" /> Share
                    </a>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default">
                  <div className="embed-responsive embed-responsive-4by3">
                    <iframe
                      className="embed-responsive-item"
                      src="//player.vimeo.com/video/50522981?title=0&amp;byline=0&amp;portrait=0&amp;color=ffffff"
                      width="100%"
                      height="271"
                      frameBorder="0"
                      webkitallowfullscreen="false"
                      mozallowfullscreen="false"
                      allowFullScreen
                    />
                  </div>

                  <div className="panel-body">
                    <p className="margin-none">Vimeo Video</p>
                  </div>
                  <div className="btn-group btn-group-footer btn-group-justified">
                    <a href="#" className="btn">
                      <i className="fa fa-heart" /> Like
                    </a>
                    <a href="#" className="btn">
                      <i className="fa fa-user" /> Share
                    </a>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default">
                  <div className="embed-responsive embed-responsive-4by3">
                    <iframe
                      className="embed-responsive-item"
                      src="//www.youtube-nocookie.com/embed/Ycv5fNd4AeM?rel=0"
                    />
                  </div>

                  <div className="panel-body">
                    <p className="margin-none">Youtube Video</p>
                  </div>
                  <div className="btn-group btn-group-footer btn-group-justified">
                    <a href="#" className="btn">
                      <i className="fa fa-heart" /> Like
                    </a>
                    <a href="#" className="btn">
                      <i className="fa fa-user" /> Share
                    </a>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <a href="#" className="h5 margin-none">
                      Climb a Mountain
                    </a>
                    <div className="text-muted">
                      <small>
                        <i className="fa fa-calendar" /> 24/10/2014
                      </small>
                    </div>
                  </div>
                  <a href="#">
                    <img
                      src="images/place1-full.jpg"
                      alt="image"
                      className="img-responsive"
                    />
                  </a>
                  <div className="panel-body">
                    <p>
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                      Dolor impedit ipsum laborum maiores tempore veritatis....
                    </p>
                    <div>
                      <div className="pull-right">
                        <a href="#" className="btn btn-primary btn-xs">
                          read
                        </a>
                      </div>

                      <a href="#" className="text-muted">
                        {" "}
                        <i className="fa fa-comments" /> 6
                      </a>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body text-center">
                    <a href="#" className="h5 margin-none">
                      Vegetarian Pizza
                    </a>
                    <p className="text-muted">
                      <i className="fa fa-calendar" /> 24/10/2014
                    </p>
                    <span className="fa fa-star text-primary" />
                    <span className="fa fa-star text-primary" />
                    <span className="fa fa-star text-primary" />
                    <span className="fa fa-star text-primary" />
                    <span className="fa fa-star-o" />
                  </div>
                  <a href="#">
                    <img
                      src="images/food1-full.jpg"
                      alt="image"
                      className="img-responsive"
                    />
                  </a>
                  <div className="panel-body">
                    <p>
                      Lorem ipsum dolor sit amet, consectetur adipisicing elit.
                      Dolor impedit ipsum laborum maiores tempore veritatis....
                    </p>
                    <div>
                      <div className="pull-right">
                        <a href="#" className="btn btn-primary btn-xs">
                          read
                        </a>
                      </div>
                      <a href="#" className="text-muted">
                        {" "}
                        <i className="fa fa-comments" /> 6
                      </a>
                    </div>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-sm-6 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body">
                    <div className="pull-right">
                      <a href="#" className="btn btn-success btn-xs">
                        <i className="fa fa-check-circle" />
                      </a>
                    </div>
                    <a href="#" className="h5">
                      Win a Holiday
                    </a>
                    <div className="text-muted">
                      <small>
                        <i className="fa fa-calendar" /> 24/10/2014
                      </small>
                    </div>
                  </div>
                  <a href="#">
                    <img
                      src="images/place2-full.jpg"
                      alt="image"
                      className="img-responsive"
                    />
                  </a>
                  <ul className="icon-list icon-list-block">
                    <li>
                      <i className="fa fa-calendar fa-fw" />{" "}
                      <a href="#">1 Week</a>
                    </li>
                    <li>
                      <i className="fa fa-users fa-fw" />{" "}
                      <a href="#"> 2 People</a>
                    </li>
                    <li>
                      <i className="fa fa-map-marker fa-fw" />{" "}
                      <a href="#">Miami, FL, USA</a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>

            <div className="page-section-heading">
              <h1 className="text-display-1">
                How much should you pay for this?
              </h1>

              <p className="lead">A lot less than you expect.</p>
            </div>
            <div className="row" data-toggle="isotope">
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body text-center">
                    <span className="pull-right label label-success">
                      &dollar; 0
                    </span>
                    <h3 className="margin-none text-headline">Free</h3>
                  </div>
                  <ul className="list-group">
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Free Signup
                    </li>
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Email
                      Address
                    </li>
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Friends
                      Management
                    </li>
                  </ul>
                  <div className="panel-body text-center">
                    <button type="submit" className="btn btn-default">
                      Purchase <i className="fa fa-shopping-cart" />
                    </button>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body text-center">
                    <span className="pull-right label label-default">
                      &dollar; 25
                    </span>
                    <h3 className="margin-none text-headline">Individual</h3>
                  </div>
                  <ul className="list-group">
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Free Signup
                    </li>
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Email
                      Address
                    </li>
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Friends
                      Management
                    </li>
                  </ul>
                  <div className="panel-body text-center">
                    <button type="submit" className="btn btn-default">
                      Purchase <i className="fa fa-shopping-cart" />
                    </button>
                  </div>
                </div>
              </div>
              <div className="item col-md-4 col-xs-12">
                <div className="panel panel-default">
                  <div className="panel-body text-center">
                    <span className="pull-right label label-primary">
                      &dollar; 50
                    </span>
                    <h3 className="margin-none text-headline">Company</h3>
                  </div>
                  <ul className="list-group">
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Free Signup
                    </li>
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Email
                      Address
                    </li>
                    <li className="list-group-item">
                      <i className="fa fa-check fa-fw text-muted" /> Friends
                      Management
                    </li>
                  </ul>
                  <div className="panel-body text-center">
                    <button type="submit" className="btn btn-default">
                      Purchase <i className="fa fa-shopping-cart" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
);

export default HomePage;

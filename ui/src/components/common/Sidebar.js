import React from "react";

const Sidebar = () => {
  return (
    <>
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
                    </ul>
                    <ul className="sidebar-menu sm-bordered sm-active-item-bg">
                      <li className="hasSubmenu">
                        <a
                          href="#submenu-media"
                          data-toggle="collapse"
                          className="collapse"
                          aria-expanded="false"
                        >
                          <i className="fa fa-photo" /> <span>Media</span>
                        </a>
                        <ul
                          id="submenu-media"
                          data-toggle="collapse"
                          className="collapse"
                          aria-expanded="false"
                        >
                          <li>
                            <a href="media-gallery.html">
                              <i className="fa fa-camera" />
                              <span>Gallery</span>
                            </a>
                          </li>
                          <li>
                            <a href="media-carousel.html">
                              <i className="fa fa-circle-o" />
                              <span>Carousels</span>
                            </a>
                          </li>
                        </ul>
                      </li>

                      <li className="hasSubmenu">
                        <a
                          href="#nav-charts"
                          data-toggle="collapse"
                          className="collapse"
                          aria-expanded="false"
                        >
                          <i className="fa fa-bar-chart" /> <span>Charts</span>
                        </a>
                        <ul
                          id="nav-charts"
                          data-toggle="collapse"
                          className="collapse"
                          aria-expanded="false"
                        >
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
    </>
  );
};

export default Sidebar;

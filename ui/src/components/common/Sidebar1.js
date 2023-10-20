import React from "react";

const Sidebar1 = () => {
  return (
    <>
      <div
        className="sidebar sidebar-chat right sidebar-size-2 sidebar-offset-0 chat-skin-white"
        id="sidebar-chat"
      >
        <div className="split-vertical">
          <div className="chat-search">
            <input type="text" className="form-control" placeholder="Search" />
          </div>

          <ul className="chat-filter nav nav-pills ">
            <li className="active">
              <a href="#" data-target="li">
                All
              </a>
            </li>
            <li>
              <a href="#" data-target=".online">
                Online
              </a>
            </li>
            <li>
              <a href="#" data-target=".offline">
                Offline
              </a>
            </li>
          </ul>
          <div className="split-vertical-body">
            <div className="split-vertical-cell">
              <div data-scrollable>
                <ul className="chat-contacts">
                  <li className="online" data-user-id="1">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/guy-6.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Jonathan S.</div>
                          <small>Free Today</small>
                        </div>
                      </div>
                    </a>
                  </li>

                  <li className="online away" data-user-id="2">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/woman-5.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Mary A.</div>
                          <small>Feeling Groovy</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="online" data-user-id="3">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left ">
                          <span className="status" />
                          <img
                            src="images/people/110/guy-3.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Adrian D.</div>
                          <small>Busy</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="offline" data-user-id="4">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <img
                            src="images/people/110/woman-6.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Michelle S.</div>
                          <small>Offline</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="offline" data-user-id="5">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <img
                            src="images/people/110/woman-7.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Daniele A.</div>
                          <small>Offline</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="online" data-user-id="6">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/guy-4.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Jake F.</div>
                          <small>Busy</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="online away" data-user-id="7">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/woman-6.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Jane A.</div>
                          <small>Custom Status</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="offline" data-user-id="8">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/woman-8.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Sabine J.</div>
                          <small>Offline right now</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="online away" data-user-id="9">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/woman-9.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Danny B.</div>
                          <small>Be Right Back</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="online" data-user-id="10">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/woman-8.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">Elise J.</div>
                          <small>My Status</small>
                        </div>
                      </div>
                    </a>
                  </li>
                  <li className="online" data-user-id="11">
                    <a href="#">
                      <div className="media">
                        <div className="pull-left">
                          <span className="status" />
                          <img
                            src="images/people/110/guy-3.jpg"
                            width="40"
                            className="img-circle"
                          />
                        </div>
                        <div className="media-body">
                          <div className="contact-name">John J.</div>
                          <small>My Status #1</small>
                        </div>
                      </div>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
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
                <img src="{{ user_image }}" width="40" />
              </span>
              <span className="contact-name" />
            </a>
          </div>
          <div className="panel-body" id="chat-bill">
            <div className="media">
              <div className="media-left">
                <img
                  src="{{ user_image }}"
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
                  src="{{ user_image }}"
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
                  src="{{ user_image }}"
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
                  src="{{ user_image }}"
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
    </>
  );
};

export default Sidebar1;

import React, { useEffect } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import Header from "../common/Header";
import Footer from "../common/Footer";
import Sidebar from "../common/Sidebar";
import refreshableFetch from "../../resources/refreshableFetch";
import moment from "moment";
import { loadMembers } from "./memberActions";
//import * as buildTable from "./jdataTable";
import { DataTable } from "react-data-components";

function buildTable(data) {
  const renderMapUrl = (val, row) => (
    <a href={`https://www.google.com/maps?q=${row["lat"]},${row["long"]}`}>
      Google Maps
    </a>
  );

  const tableColumns = [
    { title: "Name", prop: "firstName" },
    { title: "City", prop: "city" },
    { title: "Street address", prop: "street" },
    { title: "Phone", prop: "phone", defaultContent: "<no phone>" },
    { title: "Map", render: renderMapUrl, className: "text-center" }
  ];

  return (
    <DataTable
      className="table v-middle"
      keys="userId"
      columns={tableColumns}
      initialData={data}
      initialPageLength={5}
      initialSortBy={{ prop: "city", order: "descending" }}
      pageLengthOptions={[5, 20, 50]}
    />
  );
}

function MemberApp({ loading = false, error, loadMembers, members }) {
  useEffect(() => {
    refreshableFetch(null, null);
    loadMembers();
    // buildTable(members);
  }, []);
  return (
    <div className="st-container">
      <Header role="navigation" />
      <Sidebar />
      <div className="st-content" id="content">
        <div className="st-content-inner">
          <div className="container-fluid">
            <div className="page-section">
              <div className="row">{buildTable(members.data)}</div>
              <div className="row">
                <div className="col-md-10 col-lg-8 col-md-offset-1 col-lg-offset-2">
                  <div className="panel panel-default">
                    <div className="table-responsive">
                      <table className="table v-middle">
                        <thead>
                          <tr>
                            <th>Date</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Location</th>
                            <th>Progress</th>
                            <th className="text-right">Action</th>
                          </tr>
                        </thead>
                        <tbody id="responsive-table-body">
                          {members.data.map(member => {
                            return (
                              <tr key={member.userId}>
                                <td>{member.email}</td>
                                <td />
                                <td />
                                <td />
                                <td />
                                <td />
                              </tr>
                            );
                          })}
                          <tr>
                            <td>
                              <span className="label label-default">
                                19/09/2014
                              </span>
                            </td>
                            <td>
                              <img
                                src="images/people/110/guy-5.jpg"
                                width="40"
                                className="img-circle"
                              />{" "}
                              Jonathan Smith
                            </td>
                            <td>
                              <a href="#">contact@mosaicpro.biz</a>
                            </td>
                            <td>
                              Miami, FL
                              <a href="#">
                                <i className="fa fa-map-marker fa-fw text-muted" />
                              </a>
                            </td>
                            <td>
                              <div className="progress">
                                <div
                                  className="progress-bar"
                                  role="progressbar"
                                  aria-valuenow="60"
                                  aria-valuemin="0"
                                  aria-valuemax="100"
                                />
                              </div>
                            </td>
                            <td className="text-right">
                              <a
                                href="#"
                                className="btn btn-default btn-xs"
                                data-toggle="tooltip"
                                data-placement="top"
                                title="Edit"
                              >
                                <i className="fa fa-pencil" />
                              </a>
                              <a
                                href="#"
                                className="btn btn-danger btn-xs"
                                data-toggle="tooltip"
                                data-placement="top"
                                title="Delete"
                              >
                                <i className="fa fa-times" />
                              </a>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <span className="label label-primary">
                                19/09/2014
                              </span>
                            </td>
                            <td>
                              <img
                                src="images/people/110/woman-4.jpg"
                                width="40"
                                className="img-circle"
                              />{" "}
                              Michelle Smith
                            </td>
                            <td>
                              <a href="#">contact@mosaicpro.biz</a>
                            </td>
                            <td>
                              Amsterdam, EU{" "}
                              <a href="#">
                                <i className="fa fa-map-marker fa-fw text-muted" />
                              </a>
                            </td>
                            <td>
                              <div className="progress">
                                <div
                                  className="progress-bar progress-bar-success"
                                  role="progressbar"
                                  aria-valuenow="43"
                                  aria-valuemin="0"
                                  aria-valuemax="100"
                                />
                              </div>
                            </td>
                            <td className="text-right">
                              <a
                                href="#"
                                className="btn btn-default btn-xs"
                                data-toggle="tooltip"
                                data-placement="top"
                                title="Edit"
                              >
                                <i className="fa fa-pencil" />
                              </a>
                              <a
                                href="#"
                                className="btn btn-danger btn-xs"
                                data-toggle="tooltip"
                                data-placement="top"
                                title="Delete"
                              >
                                <i className="fa fa-times" />
                              </a>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>

                    <div className="panel-footer padding-none text-center">
                      <ul className="pagination">
                        <li className="disabled">
                          <a href="#">&laquo;</a>
                        </li>
                        <li className="active">
                          <a href="#">1</a>
                        </li>
                        <li>
                          <a href="#">2</a>
                        </li>
                        <li>
                          <a href="#">3</a>
                        </li>
                        <li>
                          <a href="#">4</a>
                        </li>
                        <li>
                          <a href="#">5</a>
                        </li>
                        <li>
                          <a href="#">&raquo;</a>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <Footer year={"" + moment().year()} />
    </div>
  );
}

MemberApp.propTypes = {
  loadMembers: PropTypes.func.isRequired,
  loading: PropTypes.bool,
  error: PropTypes.string,
  members: PropTypes.object.isRequired
};

function mapStateToProps(state) {
  const loading = state.apiCallsInProgress > 0;
  const { error } = state;
  const { members } = state;
  return {
    loading,
    error,
    members
  };
}

const mapDispatchToProps = {
  loadMembers
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MemberApp);

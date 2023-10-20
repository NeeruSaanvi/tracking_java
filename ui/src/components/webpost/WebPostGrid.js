import React, { useRef } from "react";
import { handleResponse, handleError, getHeaders } from "../../api/apiUtils";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import Alert from "../common/Alert";
import Spinner from "../common/Spinner";
import Grid from "../common/Grid/Grid";
import * as $ from "jquery";
import bootstrap from "bootstrap";
import Manufacturer from "../common/Manufacturer/Manufacturer";

const baseUrl = process.env.API_URL;

const linkrenderer = rec => {
  let value = rec.link.substring(0, 10);
  return <a href="#"> {value} </a>;
};

const addEditWeb = rec => {
  //alert(JSON.stringify(rec));

  $("#websiteName").val(rec.blogname);
  $("#websiteLink").val(rec.link);
  $("#blogidhid").val(rec.blogId);
  //alert("" + rec.blogid);

  $("#modal-slide-down").modal("show");
};

const WebPostGrid = props => {
  const actionrenderer = rec => {
    return (
      <>
        <a
          href="#"
          className="btn btn-default btn-xs"
          data-toggle="tooltip"
          data-placement="top"
          title="Add"
          onClick={() => addEditWeb(rec)}
        >
          <i className="fa fa-plus-square" />
        </a>
        &nbsp;
        <a
          href="#"
          className="btn btn-danger btn-xs"
          data-toggle="tooltip"
          data-placement="top"
          title="Delete"
          onClick={() => deleteWebPost(rec)}
        >
          <i className="fa fa-times" />
        </a>
      </>
    );
  };

  const deleteWebPost = rec => {
    fetch(baseUrl + "/web/deleteWebPost?blogId=" + rec.blogId, {
      method: "DELETE",
      headers: getHeaders()
    })
      .then(handleResponse)
      .then(() => {
        gridRef.current.reload();
      })
      .catch(handleError);
  };

  const config = {
    id: "webPostGrid",
    url: "/web",
    params: null,
    columns: [
      {
        headerTitle: "Action",
        id: "blogId",
        type: "renderer",
        data: "blogId",
        headerStyle: { minWidth: "180px" },
        renderer: actionrenderer
      },
      {
        headerTitle: "Date",
        id: "postedDate",
        type: "text",
        data: "postedDate",
        headerStyle: { minWidth: "100px" }
      },
      {
        headerTitle: "WebSite Name",
        id: "blogname",
        type: "text",
        data: "blogname",
        headerStyle: { minWidth: "180px" }
      },
      {
        headerTitle: "Headline",
        id: "postedDate1",
        type: "text",
        data: "postedDate",
        headerStyle: { minWidth: "100px" }
      },
      {
        headerTitle: "Link",
        id: "link",
        type: "renderer",
        data: "link",
        headerStyle: { minWidth: "280px" },
        renderer: linkrenderer
      },
      {
        headerTitle: "Sponsors",
        id: "postedDate2",
        type: "text",
        headerStyle: { minWidth: "180px" },
        data: "postedDate"
      },
      {
        headerTitle: "Impressions",
        id: "postedDate3",
        type: "text",
        headerStyle: { minWidth: "180px" },
        data: "postedDate"
      }
    ],
    itemsPerPageOptions: [10, 25, 50, 100, 500]
  };

  $.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
      if (o[this.name] !== undefined) {
        if (!o[this.name].push) {
          o[this.name] = [o[this.name]];
        }
        o[this.name].push(this.value || "");
      } else {
        o[this.name] = this.value || "";
      }
    });
    return o;
  };

  const saveWeb = e => {
    e.stopPropagation();
    e.preventDefault();

    //const data = formToJSON($("#webPostForm").elements);

    fetch(baseUrl + "/web/saveWebPost", {
      method: "POST",
      //body: $("#webPostForm").serialize(),
      body: JSON.stringify($("#webPostForm").serializeObject()),
      headers: getHeaders()
    })
      .then(handleResponse)
      .then(res => {
        gridRef.current.reload();
        $("#modal-slide-down").modal("hide");
      })
      .catch(handleError);
  };

  const gridRef = useRef();

  return (
    <>
      <div className="item col-xs-12 col-md-12">
        <div className="panel panel-default">
          <div className="panel-body">
            <Grid config={config} ref={gridRef} />
          </div>
        </div>
      </div>
      <div className="modal" id="modal-slide-down">
        <div className="modal-dialog">
          <div className="v-cell">
            <div className="modal-content modal-lg">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal">
                  <span aria-hidden="true">&times;</span>
                  <span className="sr-only">Close</span>
                </button>
                <h4 className="modal-title">Modal title</h4>
              </div>
              <div className="panel panel-default">
                <div className="panel-body">
                  <form id="webPostForm" action="saveWeb">
                    <div className="row">
                      <div className="col-md-12">
                        <div className="form-group form-control-default required">
                          <label>Website name</label>
                          <input
                            type="text"
                            className="form-control"
                            id="websiteName"
                            name="blogname"
                            placeholder="Your first name"
                          />
                          <input type="hidden" name="blogId" id="blogidhid" />
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-md-12">
                        <div className="form-group form-control-default">
                          <label>Coverage Type</label>
                          <input
                            type="email"
                            className="form-control"
                            id="coverageType"
                            name="coverageType"
                            placeholder="Your last name"
                          />
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-md-12">
                        <Manufacturer />
                      </div>
                    </div>
                    <div className="form-group form-control-default required">
                      <label>Website URL</label>
                      <input
                        type="textarea"
                        className="form-control"
                        id="websiteLink"
                        name="link"
                        placeholder="Enter url"
                      />
                    </div>

                    <div className="modal-footer">
                      <button
                        type="button"
                        className="btn btn-default"
                        data-dismiss="modal"
                      >
                        Close
                      </button>
                      <button
                        type="button"
                        className="btn btn-primary"
                        onClick={e => saveWeb(e)}
                      >
                        Save
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default WebPostGrid;

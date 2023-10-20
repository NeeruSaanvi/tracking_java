import React from "react";
import ReactDOM from "react-dom";
import { getHeaders } from "../../api/apiUtils";
import $ from "jquery";
import DataTable from "datatables.net";
import * as memberApi from "../../api/memberApi";

import Header from "../common/Header";
import Sidebar from "../common/Sidebar";

export default class GridReact extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      members: { data: [] }
    };
  }

  beforeDataSubmit(data) {}

  allParameters(data) {
    var columnLength = data.columns.length;

    var index;
    for (index = 0; index < columnLength; index++) {
      if (data.order[0]["column"] == index) {
        var obj = data.columns[index]["data"];
        if (obj instanceof Object == true) {
          data.sort = obj[0].data;
        } else {
          data.sort = data.columns[index]["data"];
        }
      }
    }
    data.dir = data.order[0]["dir"];

    delete data.columns;
    delete data.order;
    delete data.search;
    data.limit = data.length;
    var table = $("#mytable").DataTable();
    data.page = table != undefined ? table.page.info().page + 1 : 1;
    //data.page = data.draw;
    delete data.length;
  }

  getInitialState() {
    return { members: { data: [] } };
  }
  loadData() {
    $.ajax({
      url: process.env.API_URL + "/users",
      headers: getHeaders(),
      dataType: "json",
      contentType: "application/json",
      dataSrc: "data",
      data: this.allParameters
    }).then(
      function(data) {
        this.setState({ members: data });
      }.bind(this)
    );

    /*$.ajax({
      url: process.env.API_URL + "/users",
      headers: getHeaders(),
      success: function(data) {
        this.setState({ members: data });
        console.log("load data::");
      }.bind(this)
    });*/
  }
  componentWillMount() {
    console.log("componentWillMount");
    // this.loadData();

    /*memberApi.getAllMembers().then(
      function(JsonList) {
        this.setState({ members: JsonList });
      }.bind(this)
    );*/
  }

  editDeleteRenderer(data, type, row) {
    var html =
      "<table style='width: 70px;'><tr id='table_" +
      data +
      "'><td style='padding: 0px 0px !important;'>" +
      "<a onclick=\"return $.preUpdateUserLinkClick('" +
      data +
      "','" +
      row["firstName"] +
      "','" +
      row["lastName"] +
      '\'); " href="#">' +
      '<i title="Update User" data-placement="top" data-toggle="tooltip" class="tooltips glyphicon glyphicon-pencil pull-center text-muted" style=""' +
      'data-original-title="Update User"><span class="hide">&nbsp;</span></i>' +
      "</a> " +
      "</td><td style='padding: 0px 0px !important;'>" +
      "<a onclick=\"return $.deletePackuser('" +
      data +
      "','" +
      row["firstName"] +
      "','" +
      row["lastName"] +
      '\'); " href="#">' +
      '<i title="Delete User" data-placement="top" data-toggle="tooltip" class="tooltips glyphicon glyphicon-trash pull-center text-muted" style="margin-left: 5px;"' +
      'data-original-title="Delete User"><span class="hide">&nbsp;</span></i>' +
      "</a>" +
      "</td><td style='padding: 0px 0px !important;'>" +
      "<a onclick=\"return $.resetpasswrodpack('" +
      data +
      "', '" +
      row["firstName"] +
      "','" +
      row["lastName"] +
      '\'); "href="#" >' +
      '<i title="Rest password" data-placement="top" data-toggle="tooltip" class="tooltips glyphicon glyphicon-lock pull-center text-muted" style="margin-left: 5px;"' +
      'data-original-title="Rest password"><span class="hide">&nbsp;</span></i>' +
      "</a></td> ";

    $("#table_" + data).append(
      "<td style='padding: 0px 0px !important;'>" +
        "<a onclick=\"return $.loginAsSelected('" +
        data +
        "'); \"href=\"#\" data-toggle='modal' data-target='#modalForLoginAsSelected' >" +
        '<i title="Login As Selected" data-placement="top" data-toggle="tooltip" class="tooltips glyphicon glyphicon-user pull-center text-muted" style="margin-left: 5px;"' +
        'data-original-title="Login As Selected"><span class="hide">&nbsp;</span></i>' +
        "</a></td> "
    );

    $("#table_" + data).append("</tr></table>");
    return html;
  }

  getColumns() {
    var arr = [
      { data: "userId", render: this.editDeleteRenderer },
      { data: "firstName" },
      { data: "email" }
    ];
    return arr;
  }

  componentDidMount() {
    console.log("componentDidMount");
    var self = this;
    $("#mytable").dataTable({
      dom: 'rt<"bottom"ilBp><"clear">',
      processing: true,
      serverSide: true,
      lengthMenu: [[5, 15, 25, 50, -1], [5, 15, 25, 50, "All"]],
      pagingType: "full_numbers",
      paging: true,
      ordering: true,
      info: true,
      //ajax: this.loadData
      ajax: {
        url: process.env.API_URL + "/users/datatable",
        dataType: "json",
        contentType: "application/json",
        dataSrc: "data",
        data: this.allParameters
      },
      columns: this.getColumns()

      /*sPaginationType: "bootstrap",
      bAutoWidth: false,
      bDestroy: true,
      fnDrawCallback() {
        self.forceUpdate();
      }*/
    });
  }
  componentDidUpdate() {
    console.log("componentDidUpdate");
    /*$("#mytable").dataTable({
      dom: 'rt<"bottom"ilBp><"clear">',
      lengthMenu: [[5, 15, 25, 50, -1], [5, 15, 25, 50, "All"]]      
    });*/
    /*const table = $("#mytable")
      .find("table")
      .DataTable();

    table.clear();
    //table.rows.add(this.transform(this.props.members));
    table.draw();*/
  }
  render() {
    var x = this.state.members.data.map(function(d, index) {
      return (
        <tr key={index}>
          <td>{index + 1}</td>
          <td>{d.firstName}</td>
          <td>{d.firstName}</td>
        </tr>
      );
    });
    return (
      <div className="st-container">
        <Header role="navigation" />
        <Sidebar />
        <div className="st-content" id="content">
          <div className="st-content-inner">
            <div className="container-fluid">
              <div className="page-section">
                <div className="row">
                  <div className="panel panel-default">
                    <div className="table-responsive">
                      <table className="table table-striped" id="mytable">
                        <thead>
                          <tr>
                            <th />
                            <th>Name</th>
                            <th>Email</th>
                          </tr>
                        </thead>
                        <tbody />
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

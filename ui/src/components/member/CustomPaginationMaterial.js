import * as React from "react";
//import $ from "jquery";
import * as $ from "jquery";
import bootstrap from "bootstrap";

import Paper from "@material-ui/core/Paper";
import { PagingState, CustomPaging } from "@devexpress/dx-react-grid";
import {
  Grid,
  Table,
  TableHeaderRow,
  PagingPanel
} from "@devexpress/dx-react-grid-material-ui";
import Header from "../common/Header";
import Sidebar from "../common/Sidebar";
import { Loading } from "./Loading";
import { getHeaders } from "../../api/apiUtils";
import {
  Dialog,
  TextField,
  withStyles,
  FlatButton,
  MuiThemeProvider,
  getMuiTheme
} from "@material-ui/core";
//import { withStyles } from "@material-ui/styles";

const URL = process.env.API_URL + "/users";

export default class CustomPaginationMaterial extends React.PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      columns: [
        { name: "userId", title: "Action" },
        { name: "firstName", title: "Name" },
        { name: "email", title: "Email" },
        { name: "phone", title: "Phone" }
      ],
      rows: [],
      totalCount: 0,
      pageSize: 6,
      currentPage: 0,
      loading: true,
      open: false
    };

    this.changeCurrentPage = this.changeCurrentPage.bind(this);
    this.Cell = this.Cell.bind(this);
  }

  Cell = props => {
    const { column, value, open, row } = props;
    if (column.name === "userId") {
      return (
        <Table.Cell
          {...props}
          onClick={() => $("#modal-slide-down").modal("show")}
          //onClick={() => alert(JSON.stringify(row))}
        >
          <button
            data-toggle="modal-slide-down"
            data-modal-options="slide-down"
            data-content-options="modal-lg"
            className="btn btn-primary"
            //onClick={() => $("#modal-slide-down").modal("show")}
          >
            {value}
          </button>
        </Table.Cell>
      );
    }
    return <Table.Cell {...props} />;
  };

  handleClickOpen() {
    this.setState({ open: true });
  }

  componentDidMount() {
    //$("#modal-slide-down").modal("show");
    this.loadData();
  }

  componentDidUpdate() {
    this.loadData();
  }

  changeCurrentPage(currentPage) {
    this.setState({
      loading: true,
      currentPage
    });
  }

  queryString() {
    const { pageSize, currentPage } = this.state;

    return `${URL}?size=${pageSize}&skip=${pageSize *
      currentPage}&page=${currentPage}`;
  }

  loadData() {
    const queryString = this.queryString();
    if (queryString === this.lastQuery) {
      this.setState({ loading: false });
      return;
    }

    fetch(queryString, {
      method: "GET",
      headers: getHeaders()
    })
      .then(response => response.json())
      .then(data =>
        this.setState({
          rows: data.data,
          totalCount: data.totalCount,
          loading: false
        })
      )
      .catch(() => this.setState({ loading: false }));
    this.lastQuery = queryString;
  }

  render() {
    const {
      rows,
      columns,
      pageSize,
      currentPage,
      totalCount,
      loading
    } = this.state;

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
                    <Paper style={{ position: "relative" }}>
                      <Grid rows={rows} columns={columns}>
                        <PagingState
                          currentPage={currentPage}
                          onCurrentPageChange={this.changeCurrentPage}
                          pageSize={pageSize}
                        />
                        <CustomPaging totalCount={totalCount} />
                        <Table cellComponent={this.Cell} />
                        <TableHeaderRow />
                        <PagingPanel />
                      </Grid>
                      {loading && <Loading />}
                    </Paper>

                    <div
                      className="modal slide-down fade"
                      id="modal-slide-down"
                    >
                      <div className="modal-dialog">
                        <div className="v-cell">
                          <div className="modal-content">
                            <div className="modal-header">
                              <button
                                type="button"
                                className="close"
                                data-dismiss="modal"
                              >
                                <span aria-hidden="true">&times;</span>
                                <span className="sr-only">Close</span>
                              </button>
                              <h4 className="modal-title">Modal title</h4>
                            </div>
                            <div className="modal-body">
                              Lorem ipsum dolor sit amet, consectetur
                              adipisicing elit. A aperiam atque consequuntur
                              dolore fugiat fugit hic in ipsam iure magnam
                              maxime quaerat, quam qui repellat repellendus
                              temporibus vel vitae voluptate!
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
                                data-dismiss="modal"
                              >
                                Save
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div>
                      <Dialog
                        //fullScreen
                        title="Edit User"
                        maxWidth="lg"
                        //width="70%"
                        modal="true"
                        classes={{
                          paper: "vipul-class"
                        }}
                        open={this.state.open}
                      >
                        <form
                          action="/"
                          method="POST"
                          onSubmit={e => {
                            e.preventDefault();
                            alert("Submitted form!");
                            this.handleClose();
                          }}
                        >
                          This dialog spans the entire width of the screen.
                          <TextField name="email" hinttext="email" />
                          <TextField
                            name="pwd"
                            type="password"
                            hinttext="Password"
                          />
                          <div
                            style={{
                              textAlign: "right",
                              padding: 8,
                              margin: "24px -24px -24px -24px"
                            }}
                          />
                        </form>
                      </Dialog>
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

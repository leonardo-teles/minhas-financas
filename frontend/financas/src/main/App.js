import React from 'react';

import Rotas from './rotas';
import Navbar from '../components/navbar';

import '../custom.css';
import 'toastr/build/toastr.min.css';
import 'bootswatch/dist/flatly/bootstrap.min.css';

import 'toastr/build/toastr.min.js';

class App extends React.Component {
  render(){
    return(
        <>
          <Navbar/>
          <div className="container">
              <Rotas />
          </div>
        </>
    )
  }
}

export default App;

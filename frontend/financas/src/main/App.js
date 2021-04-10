import React from 'react';

import Rotas from './rotas';
import Navbar from '../components/navbar';

import 'bootswatch/dist/flatly/bootstrap.min.css';
import 'toastr/build/toastr.min.css';
import '../custom.css';

import 'toastr/build/toastr.min.js';

import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

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

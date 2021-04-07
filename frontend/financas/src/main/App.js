import React from 'react';

import Rotas from './rotas';

import '../custom.css';
import 'bootswatch/dist/flatly/bootstrap.min.css';

class App extends React.Component {
  render(){
    return(
      <div>
          <Rotas />
      </div>
    )
  }
}

export default App;

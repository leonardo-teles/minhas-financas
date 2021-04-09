import React from 'react';
import { withRouter } from 'react-router-dom';

import UsuarioService from '../app/service/usuarioService';

import { mensagemErro, mensagemSucesso } from '../components/toastr';
import Card from '../components/card';
import FormGroup from '../components/form-group';

class CadastroUsuario extends React.Component {

    state = {
        nome: '',
        email: '',
        senha: '',
        senhaRepeticao: ''
    }

    constructor() {
        super();
        this.service = new UsuarioService(); 
    }

    cadastrar = () => {
        const usuario = {
            nome: this.state.nome,
            email: this.state.email,
            senha: this.state.senha
        }
        
        this.service
            .salvar(usuario)
            .then(response => {
                mensagemSucesso('Usuário cadastrado com sucesso. Faça login para acessar o sistema.') 
                this.props.history.push('/login');
            })
            .catch(error => {
                mensagemErro(error.response.data);
            });     
    }

    cancelar = () => {
        this.props.history.push('/login');
    }

    render() {
        return(            
            <Card title="Cadastro de Usuário">
                <div className="row">
                    <div className="col-lg-12">
                        <div className="bs-component">

                            <FormGroup label="Nome: *" htmlFor="inputNome">
                                <input type="text" 
                                        id="inputNome" 
                                        name="nome" 
                                        className="form-control" 
                                        onChange={e => this.setState({nome: e.target.value})}/>
                            </FormGroup>

                            <FormGroup label="e-Mail: *" htmlFor="inputEmail">
                                <input type="email" 
                                        id="inputEmail" 
                                        name="email"
                                        className="form-control"
                                        onChange={e => this.setState({email: e.target.value})}/>
                            </FormGroup>

                            <FormGroup label="Senha: *" htmlFor="inputSenha">
                                <input type="password" 
                                        id="inputSenha" 
                                        name="senha"
                                        className="form-control"
                                        onChange={e => this.setState({senha: e.target.value})}/>
                            </FormGroup>

                            <FormGroup label="Repita a senha: *" htmlFor="inputRepitaSenha">
                                <input type="password" 
                                        id="inputRepitaSenha" 
                                        name="senha"
                                        className="form-control"
                                        onChange={e => this.setState({senhaRepeticao: e.target.value})}/>
                            </FormGroup>
                            <button onClick={this.cadastrar} className="btn btn-success">Salvar</button>
                            <button onClick={this.cancelar} className="btn btn-danger">Cancelar</button>

                        </div>
                    </div>
                </div>
            </Card>            
        )
    }
}

export default withRouter(CadastroUsuario);
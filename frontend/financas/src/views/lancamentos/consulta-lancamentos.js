import React from 'react';
import { withRouter } from 'react-router-dom';

import LancamentoService from '../../app/service/lancamentoService';
import LocalStorageService from '../../app/service/localStorageService';
import Card from '../../components/card';
import FormGroup from '../../components/form-group';
import SelectMenu from '../../components/selectMenu';
import LancamentosTable from './lancamentosTable';

import * as mensagens from '../../components/toastr';

import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
 
class ConsultaLancamentos extends React.Component {

    state = {
        ano: '',
        mes: '',
        tipo: '',
        descricao: '',
        showConfirDialog: false,
        lancamentoDeletar: {},
        lancamentos: []
    }

    constructor() {
        super();
        this.service = new LancamentoService();
    }

    buscar = () => {
        if(!this.state.ano) {
            mensagens.mensagemErro('O preenchimento do campo ano é obrigatório');

            return false;
        }

        const usuarioLogado = LocalStorageService.obterItem('_usuario_logado');

        const lancamentoFiltro = {
            ano: this.state.ano,
            mes: this.state.mes,
            tipo: this.state.tipo,
            descricao: this.state.descricao,
            usuario: usuarioLogado.id
        }

        this.service
            .consultar(lancamentoFiltro)
            .then(response => {
                this.setState({lancamentos: response.data})
            }).catch(error => {
                console.log(error);
            })
    }

    editar = (id) => {
        this.props.history.push(`/cadastro-lancamentos/${id}`);
    }

    abrirConfirmacaoExclusao = (lancamento) => {
        this.setState({showConfirDialog: true, lancamentoDeletar: lancamento});
    }

    cancelarDelecao = () => {
        this.setState({showConfirDialog: false, lancamentoDeletar: {}});
    }

    alterarStatus = (lancamento, status) => {
        this.service
            .alterarStatus(lancamento.id, status)
            .then(response => {
                const lancamentos = this.state.lancamentos;
                const index = lancamentos.indexOf(lancamento);

                if(index !== -1) {
                    lancamento['status'] = status;
                    lancamentos[index] = lancamento;
                    this.setState({lancamento});
                }

                mensagens.mensagemSucesso('Status atualizado com sucesso');
            })
    }

    deletar = () => {
        this.service
            .deletar(this.state.lancamentoDeletar.id)
            .then(response => {
                const lancamentos = this.state.lancamentos;
                const index = lancamentos.indexOf(this.state.lancamentoDeletar)
                lancamentos.splice(index, 1);

                this.setState({lancamentos: lancamentos, showConfirDialog: false});

                mensagens.mensagemSucesso('Lançamento excluído com sucesso');
            }).catch(error => {
                mensagens.mensagemErro('Ocorreu um erro ao tentar deletar um lançamento');
            })
    }

    preparaFormularioCadastro = () => {
        this.props.history.push('/cadastro-lancamentos');
    }

    render() {
        const meses = this.service.obterListaMeses();

        const tipos = this.service.obterListaTipos();

        const confirmDialogFooter = (
            <div>
                <Button label="Cancela" icon="pi pi-times" onClick={this.cancelarDelecao} className="p-button-text" />
                <Button label="Confirma" icon="pi pi-check" onClick={this.deletar} />
            </div>            
        );
    

        return(
            <Card title="Consultar Lançamentos">
                <div className="row">
                    <div className="col-md-6">
                        <div className="bs-component">
                            <FormGroup htmlFor="inputAno" label="Ano: *">
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    id="inputAno" 
                                    value={this.state.ano}
                                    onChange={e => this.setState({ano: e.target.value})}
                                    placeholder="Digite o Ano"/>
                            </FormGroup>

                            <FormGroup htmlFor="inputMes" label="Mês: *">
                                <SelectMenu 
                                    id="inputMes" 
                                    value={this.state.mes}
                                    onChange={e => this.setState({mes: e.target.value})}
                                    className="form-control"  
                                    lista={meses}/>
                            </FormGroup>

                            <FormGroup htmlFor="inputDescricao" label="Descrição: ">
                                <input 
                                    type="text" 
                                    className="form-control" 
                                    id="inputDescricao" 
                                    value={this.state.descricao}
                                    onChange={e => this.setState({descricao: e.target.value})}
                                    placeholder="Digite a descrição"/>
                            </FormGroup>

                            <FormGroup htmlFor="inputTipo" label="Tipo Lançamento: *">
                                <SelectMenu 
                                    id="inputTipo" 
                                    value={this.state.tipo}
                                    onChange={e => this.setState({tipo: e.target.value})}
                                    className="form-control"  
                                    lista={tipos}/>
                            </FormGroup>

                            <button onClick={this.buscar} type="button" className="btn btn-success">Buscar</button>
                            <button onClick={this.preparaFormularioCadastro} type="button" className="btn btn-danger">Cadastrar</button>
                        </div>
                    </div>
                </div>
                <br/>
                <div className="row">
                    <div className="col-md-12">
                        <div className="bs-component">
                            <LancamentosTable 
                                lancamentos={this.state.lancamentos} 
                                deletar={this.abrirConfirmacaoExclusao}
                                editar={this.editar}
                                alterarStatus={this.alterarStatus}
                            />
                        </div>
                    </div>
                </div>
                <div>
                    <Dialog header="Confirmação" 
                            visible={this.state.showConfirDialog} 
                            style={{ width: '50vw' }} 
                            footer={confirmDialogFooter}
                            onHide={() => this.setState({showConfirDialog: false})}>
                        Confirma a exclusão deste lançamento?        
                    </Dialog>                    
                </div>

            </Card>
        )
    }

}

export default withRouter(ConsultaLancamentos);
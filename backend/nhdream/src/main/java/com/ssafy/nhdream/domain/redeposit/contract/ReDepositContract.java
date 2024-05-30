package com.ssafy.nhdream.domain.redeposit.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.3.
 */
@SuppressWarnings("rawtypes")
public class ReDepositContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5060405162001022380380620010228339818101604052810190610034919061011e565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505061014b565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006100eb826100c0565b9050919050565b6100fb816100e0565b811461010657600080fd5b50565b600081519050610118816100f2565b92915050565b600060208284031215610134576101336100bb565b5b600061014284828501610109565b91505092915050565b610ec7806200015b6000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80631e347d8d14610067578063636565fc146100855780636df9fb54146100a157806378aa07db146100d45780638da5cb5b146100f0578063aaf103981461010e575b600080fd5b61006f61012a565b60405161007c919061084e565b60405180910390f35b61009f600480360381019061009a91906108e0565b61014e565b005b6100bb60048036038101906100b69190610985565b610359565b6040516100cb94939291906109ef565b60405180910390f35b6100ee60048036038101906100e99190610a34565b6103a3565b005b6100f86105f8565b6040516101059190610a70565b60405180910390f35b61012860048036038101906101239190610a8b565b61061e565b005b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff1660e01b81526004016101ab93929190610acb565b6020604051808303816000875af11580156101ca573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906101ee9190610b2e565b61022d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161022490610bb8565b60405180910390fd5b6000814261023b9190610c07565b9050604051806080016040528085815260200184815260200182815260200160001515815250600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008763ffffffff1663ffffffff16815260200190815260200160002060008201518160000155602082015181600101556040820151816002015560608201518160030160006101000a81548160ff0219169083151502179055509050503373ffffffffffffffffffffffffffffffffffffffff167f32d27694d236e981c4f4f4ad272828b6b53796c5eee4a822364a143a21bbc00d8686868560405161034a9493929190610c4a565b60405180910390a25050505050565b6002602052816000526040600020602052806000526040600020600091509150508060000154908060010154908060020154908060030160009054906101000a900460ff16905084565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008363ffffffff1663ffffffff1681526020019081526020016000209050806002015442101561044a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161044190610cdb565b60405180910390fd5b600015158160030160009054906101000a900460ff161515146104a2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161049990610d47565b60405180910390fd5b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb3383600001546040518363ffffffff1660e01b8152600401610501929190610d67565b6020604051808303816000875af1158015610520573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906105449190610b2e565b610583576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161057a90610ddc565b60405180910390fd5b60018160030160006101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff167fd27d07f78406b30c94affa5e0deae8dccefcc57f6d9812b6de24dbaa539c50c38383600001546040516105ec929190610dfc565b60405180910390a25050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008463ffffffff1663ffffffff16815260200190815260200160002090508060030160009054906101000a900460ff16156106d0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106c790610e71565b60405180910390fd5b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330856040518463ffffffff1660e01b815260040161072d93929190610acb565b6020604051808303816000875af115801561074c573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906107709190610b2e565b6107af576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016107a690610bb8565b60405180910390fd5b818160000160008282546107c39190610c07565b92505081905550505050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b600061081461080f61080a846107cf565b6107ef565b6107cf565b9050919050565b6000610826826107f9565b9050919050565b60006108388261081b565b9050919050565b6108488161082d565b82525050565b6000602082019050610863600083018461083f565b92915050565b600080fd5b600063ffffffff82169050919050565b6108878161086e565b811461089257600080fd5b50565b6000813590506108a48161087e565b92915050565b6000819050919050565b6108bd816108aa565b81146108c857600080fd5b50565b6000813590506108da816108b4565b92915050565b600080600080608085870312156108fa576108f9610869565b5b600061090887828801610895565b9450506020610919878288016108cb565b935050604061092a878288016108cb565b925050606061093b878288016108cb565b91505092959194509250565b6000610952826107cf565b9050919050565b61096281610947565b811461096d57600080fd5b50565b60008135905061097f81610959565b92915050565b6000806040838503121561099c5761099b610869565b5b60006109aa85828601610970565b92505060206109bb85828601610895565b9150509250929050565b6109ce816108aa565b82525050565b60008115159050919050565b6109e9816109d4565b82525050565b6000608082019050610a0460008301876109c5565b610a1160208301866109c5565b610a1e60408301856109c5565b610a2b60608301846109e0565b95945050505050565b600060208284031215610a4a57610a49610869565b5b6000610a5884828501610895565b91505092915050565b610a6a81610947565b82525050565b6000602082019050610a856000830184610a61565b92915050565b60008060408385031215610aa257610aa1610869565b5b6000610ab085828601610895565b9250506020610ac1858286016108cb565b9150509250929050565b6000606082019050610ae06000830186610a61565b610aed6020830185610a61565b610afa60408301846109c5565b949350505050565b610b0b816109d4565b8114610b1657600080fd5b50565b600081519050610b2881610b02565b92915050565b600060208284031215610b4457610b43610869565b5b6000610b5284828501610b19565b91505092915050565b600082825260208201905092915050565b7f5472616e73666572206661696c65640000000000000000000000000000000000600082015250565b6000610ba2600f83610b5b565b9150610bad82610b6c565b602082019050919050565b60006020820190508181036000830152610bd181610b95565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000610c12826108aa565b9150610c1d836108aa565b9250828201905080821115610c3557610c34610bd8565b5b92915050565b610c448161086e565b82525050565b6000608082019050610c5f6000830187610c3b565b610c6c60208301866109c5565b610c7960408301856109c5565b610c8660608301846109c5565b95945050505050565b7f4163636f756e74206e6f74206d61747572656420796574000000000000000000600082015250565b6000610cc5601783610b5b565b9150610cd082610c8f565b602082019050919050565b60006020820190508181036000830152610cf481610cb8565b9050919050565b7f4163636f756e7420616c726561647920736574746c6564000000000000000000600082015250565b6000610d31601783610b5b565b9150610d3c82610cfb565b602082019050919050565b60006020820190508181036000830152610d6081610d24565b9050919050565b6000604082019050610d7c6000830185610a61565b610d8960208301846109c5565b9392505050565b7f4661696c656420746f2072657475726e207072696e636970616c000000000000600082015250565b6000610dc6601a83610b5b565b9150610dd182610d90565b602082019050919050565b60006020820190508181036000830152610df581610db9565b9050919050565b6000604082019050610e116000830185610c3b565b610e1e60208301846109c5565b9392505050565b7f4163636f756e742068617320616c7265616479206d6174757265640000000000600082015250565b6000610e5b601b83610b5b565b9150610e6682610e25565b602082019050919050565b60006020820190508181036000830152610e8a81610e4e565b905091905056fea26469706673582212209292381c2ac4d4b6d6218d2562c16277de1991a4554935291abbd47a5f7ec9ce64736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ACCOUNTS = "accounts";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_NHDCTOKEN = "nhdcToken";

    public static final String FUNC_OPENACCOUNT = "openAccount";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RETURNPRINCIPAL = "returnPrincipal";

    public static final Event NEWACCOUNT_EVENT = new Event("NewAccount",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event PRINCIPALRETURNED_EVENT = new Event("PrincipalReturned",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint32>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected ReDepositContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ReDepositContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ReDepositContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ReDepositContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<NewAccountEventResponse> getNewAccountEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NEWACCOUNT_EVENT, transactionReceipt);
        ArrayList<NewAccountEventResponse> responses = new ArrayList<NewAccountEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewAccountEventResponse typedResponse = new NewAccountEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.optionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.interestRate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.maturityTime = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NewAccountEventResponse getNewAccountEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NEWACCOUNT_EVENT, log);
        NewAccountEventResponse typedResponse = new NewAccountEventResponse();
        typedResponse.log = log;
        typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.optionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.balance = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.interestRate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.maturityTime = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<NewAccountEventResponse> newAccountEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNewAccountEventFromLog(log));
    }

    public Flowable<NewAccountEventResponse> newAccountEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWACCOUNT_EVENT));
        return newAccountEventFlowable(filter);
    }

    public static List<PrincipalReturnedEventResponse> getPrincipalReturnedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PRINCIPALRETURNED_EVENT, transactionReceipt);
        ArrayList<PrincipalReturnedEventResponse> responses = new ArrayList<PrincipalReturnedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PrincipalReturnedEventResponse typedResponse = new PrincipalReturnedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.optionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amountReturned = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static PrincipalReturnedEventResponse getPrincipalReturnedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRINCIPALRETURNED_EVENT, log);
        PrincipalReturnedEventResponse typedResponse = new PrincipalReturnedEventResponse();
        typedResponse.log = log;
        typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.optionId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.amountReturned = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<PrincipalReturnedEventResponse> principalReturnedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getPrincipalReturnedEventFromLog(log));
    }

    public Flowable<PrincipalReturnedEventResponse> principalReturnedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PRINCIPALRETURNED_EVENT));
        return principalReturnedEventFlowable(filter);
    }

    public RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, Boolean>> accounts(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_ACCOUNTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0),
                        new org.web3j.abi.datatypes.generated.Uint32(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple4<BigInteger, BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple4<BigInteger, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple4<BigInteger, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, BigInteger, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> deposit(BigInteger _optionId, BigInteger _amount) {
        final Function function = new Function(
                FUNC_DEPOSIT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_optionId),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> nhdcToken() {
        final Function function = new Function(FUNC_NHDCTOKEN,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> openAccount(BigInteger _optionId, BigInteger _amount, BigInteger _interestRate, BigInteger _maturityPeriodInSeconds) {
        final Function function = new Function(
                FUNC_OPENACCOUNT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_optionId),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount),
                        new org.web3j.abi.datatypes.generated.Uint256(_interestRate),
                        new org.web3j.abi.datatypes.generated.Uint256(_maturityPeriodInSeconds)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> returnPrincipal(BigInteger _optionId) {
        final Function function = new Function(
                FUNC_RETURNPRINCIPAL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_optionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ReDepositContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ReDepositContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ReDepositContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ReDepositContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ReDepositContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ReDepositContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ReDepositContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ReDepositContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ReDepositContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(ReDepositContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<ReDepositContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(ReDepositContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ReDepositContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(ReDepositContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ReDepositContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(ReDepositContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    public static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class NewAccountEventResponse extends BaseEventResponse {
        public String user;

        public BigInteger optionId;

        public BigInteger balance;

        public BigInteger interestRate;

        public BigInteger maturityTime;
    }

    public static class PrincipalReturnedEventResponse extends BaseEventResponse {
        public String user;

        public BigInteger optionId;

        public BigInteger amountReturned;
    }
}

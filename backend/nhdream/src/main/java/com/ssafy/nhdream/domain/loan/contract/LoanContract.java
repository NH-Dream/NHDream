package com.ssafy.nhdream.domain.loan.contract;

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
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple6;
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
public class LoanContract extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b506040516200141138038062001411833981810160405281019062000037919062000131565b806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060006002819055505062000163565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000620000f982620000cc565b9050919050565b6200010b81620000ec565b81146200011757600080fd5b50565b6000815190506200012b8162000100565b92915050565b6000602082840312156200014a5762000149620000c7565b5b60006200015a848285016200011a565b91505092915050565b61129e80620001736000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c80635c64403e1161005b5780635c64403e146101355780638da5cb5b146101535780638e14815214610171578063be3be0121461018f57610088565b80631e347d8d1461008d57806330ea0326146100ab5780633f81cba4146100e057806341d7ef8d14610119575b600080fd5b6100956101ab565b6040516100a29190610abf565b60405180910390f35b6100c560048036038101906100c09190610b59565b6101cf565b6040516100d796959493929190610bdc565b60405180910390f35b6100fa60048036038101906100f59190610b59565b61028a565b6040516101109a99989796959493929190610c3d565b60405180910390f35b610133600480360381019061012e9190610d05565b610318565b005b61013d610738565b60405161014a9190610dbb565b60405180910390f35b61015b61073e565b6040516101689190610de5565b60405180910390f35b610179610764565b6040516101869190610dbb565b60405180910390f35b6101a960048036038101906101a49190610e00565b61076e565b005b60008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000806000806000806000600360008a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008963ffffffff1663ffffffff168152602001908152602001600020905080600001548160050160009054906101000a900463ffffffff168260060154836003015484600401548560090160009054906101000a900460ff16965096509650965096509650509295509295509295565b6003602052816000526040600020602052806000526040600020600091509150508060000154908060010154908060020154908060030154908060040154908060050160009054906101000a900463ffffffff16908060060154908060070160009054906101000a900463ffffffff16908060080154908060090160009054906101000a900460ff1690508a565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146103a8576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161039f90610eb0565b60405180910390fd5b6000600360008973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008a63ffffffff1663ffffffff168152602001908152602001600020600001541461044a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161044190610f1c565b60405180910390fd5b600082426104589190610f6b565b9050600086886104689190610f6b565b9050604051806101400160405280898152602001888152602001828152602001848152602001600081526020018763ffffffff1681526020018381526020018663ffffffff16815260200142815260200160001515815250600360008b73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008c63ffffffff1663ffffffff168152602001908152602001600020600082015181600001556020820151816001015560408201518160020155606082015181600301556080820151816004015560a08201518160050160006101000a81548163ffffffff021916908363ffffffff16021790555060c0820151816006015560e08201518160070160006101000a81548163ffffffff021916908363ffffffff16021790555061010082015181600801556101208201518160090160006101000a81548160ff02191690831515021790555090505060008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb8a8a6040518363ffffffff1660e01b8152600401610630929190610f9f565b6020604051808303816000875af115801561064f573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906106739190610ff4565b6106b2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016106a99061106d565b60405180910390fd5b87600260008282546106c49190610f6b565b925050819055508963ffffffff168973ffffffffffffffffffffffffffffffffffffffff167f47938e876387d6611d5335b155bf51d1a371dbc9984f6c05d5b08ee60035e0ea8a8a85888c8c8a604051610724979695949392919061108d565b60405180910390a350505050505050505050565b60025481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000600254905090565b6000600360003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008363ffffffff1663ffffffff16815260200190815260200160002090508060090160009054906101000a900460ff1615610820576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161081790611148565b60405180910390fd5b60008160030154905060008054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330846040518463ffffffff1660e01b815260040161088693929190611168565b6020604051808303816000875af11580156108a5573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906108c99190610ff4565b610908576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108ff906111eb565b60405180910390fd5b8082600401600082825461091c9190610f6b565b925050819055504282600801819055506000826001015483600001546109429190610f6b565b9050808360040154106109df5760018360090160006101000a81548160ff021916908315150217905550826000015460026000828254610982919061120b565b925050819055508363ffffffff163373ffffffffffffffffffffffffffffffffffffffff167f91826a545ea0a7ec9f188175269659c4a333b2d93b2f82823d54ab3822721d09836040516109d69190610dbb565b60405180910390a35b8363ffffffff163373ffffffffffffffffffffffffffffffffffffffff167ff627b08985c7f87a70ad68ea028a3c5715f283824bd3b91ab0ccca29ba2191e1848660040154604051610a3292919061123f565b60405180910390a350505050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000610a85610a80610a7b84610a40565b610a60565b610a40565b9050919050565b6000610a9782610a6a565b9050919050565b6000610aa982610a8c565b9050919050565b610ab981610a9e565b82525050565b6000602082019050610ad46000830184610ab0565b92915050565b600080fd5b6000610aea82610a40565b9050919050565b610afa81610adf565b8114610b0557600080fd5b50565b600081359050610b1781610af1565b92915050565b600063ffffffff82169050919050565b610b3681610b1d565b8114610b4157600080fd5b50565b600081359050610b5381610b2d565b92915050565b60008060408385031215610b7057610b6f610ada565b5b6000610b7e85828601610b08565b9250506020610b8f85828601610b44565b9150509250929050565b6000819050919050565b610bac81610b99565b82525050565b610bbb81610b1d565b82525050565b60008115159050919050565b610bd681610bc1565b82525050565b600060c082019050610bf16000830189610ba3565b610bfe6020830188610bb2565b610c0b6040830187610ba3565b610c186060830186610ba3565b610c256080830185610ba3565b610c3260a0830184610bcd565b979650505050505050565b600061014082019050610c53600083018d610ba3565b610c60602083018c610ba3565b610c6d604083018b610ba3565b610c7a606083018a610ba3565b610c876080830189610ba3565b610c9460a0830188610bb2565b610ca160c0830187610ba3565b610cae60e0830186610bb2565b610cbc610100830185610ba3565b610cca610120830184610bcd565b9b9a5050505050505050505050565b610ce281610b99565b8114610ced57600080fd5b50565b600081359050610cff81610cd9565b92915050565b600080600080600080600080610100898b031215610d2657610d25610ada565b5b6000610d348b828c01610b44565b9850506020610d458b828c01610b08565b9750506040610d568b828c01610cf0565b9650506060610d678b828c01610cf0565b9550506080610d788b828c01610b44565b94505060a0610d898b828c01610b44565b93505060c0610d9a8b828c01610cf0565b92505060e0610dab8b828c01610cf0565b9150509295985092959890939650565b6000602082019050610dd06000830184610ba3565b92915050565b610ddf81610adf565b82525050565b6000602082019050610dfa6000830184610dd6565b92915050565b600060208284031215610e1657610e15610ada565b5b6000610e2484828501610b44565b91505092915050565b600082825260208201905092915050565b7f4f6e6c79206f776e65722063616e20706572666f726d2074686973206163746960008201527f6f6e000000000000000000000000000000000000000000000000000000000000602082015250565b6000610e9a602283610e2d565b9150610ea582610e3e565b604082019050919050565b60006020820190508181036000830152610ec981610e8d565b9050919050565b7f4c6f616e20494420616c72656164792065786973747300000000000000000000600082015250565b6000610f06601683610e2d565b9150610f1182610ed0565b602082019050919050565b60006020820190508181036000830152610f3581610ef9565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000610f7682610b99565b9150610f8183610b99565b9250828201905080821115610f9957610f98610f3c565b5b92915050565b6000604082019050610fb46000830185610dd6565b610fc16020830184610ba3565b9392505050565b610fd181610bc1565b8114610fdc57600080fd5b50565b600081519050610fee81610fc8565b92915050565b60006020828403121561100a57611009610ada565b5b600061101884828501610fdf565b91505092915050565b7f4661696c656420746f207472616e73666572206c6f616e2066756e6473000000600082015250565b6000611057601d83610e2d565b915061106282611021565b602082019050919050565b600060208201905081810360008301526110868161104a565b9050919050565b600060e0820190506110a2600083018a610ba3565b6110af6020830189610ba3565b6110bc6040830188610ba3565b6110c96060830187610ba3565b6110d66080830186610bb2565b6110e360a0830185610bb2565b6110f060c0830184610ba3565b98975050505050505050565b7f4c6f616e20616c72656164792072657061696400000000000000000000000000600082015250565b6000611132601383610e2d565b915061113d826110fc565b602082019050919050565b6000602082019050818103600083015261116181611125565b9050919050565b600060608201905061117d6000830186610dd6565b61118a6020830185610dd6565b6111976040830184610ba3565b949350505050565b7f4661696c656420746f206d616b65206d6f6e74686c79207061796d656e740000600082015250565b60006111d5601e83610e2d565b91506111e08261119f565b602082019050919050565b60006020820190508181036000830152611204816111c8565b9050919050565b600061121682610b99565b915061122183610b99565b925082820390508181111561123957611238610f3c565b5b92915050565b60006040820190506112546000830185610ba3565b6112616020830184610ba3565b939250505056fea2646970667358221220aea00ca03f2ccffd8d81c8f08a111baf60591555d0b363267c0a65487123969464736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_GETLOANINFO = "getLoanInfo";

    public static final String FUNC_GETTOTALACTIVELOANAMOUNT = "getTotalActiveLoanAmount";

    public static final String FUNC_ISSUELOAN = "issueLoan";

    public static final String FUNC_LOANS = "loans";

    public static final String FUNC_MAKEMONTHLYPAYMENT = "makeMonthlyPayment";

    public static final String FUNC_NHDCTOKEN = "nhdcToken";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_TOTALACTIVELOANAMOUNT = "totalActiveLoanAmount";

    public static final Event LOANFUNDSREQUESTED_EVENT = new Event("LoanFundsRequested", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}));
    ;

    public static final Event LOANISSUED_EVENT = new Event("LoanIssued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint32>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}, new TypeReference<Uint32>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event LOANREPAID_EVENT = new Event("LoanRepaid", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint32>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MONTHLYPAYMENTMADE_EVENT = new Event("MonthlyPaymentMade", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint32>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected LoanContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LoanContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LoanContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LoanContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<LoanFundsRequestedEventResponse> getLoanFundsRequestedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LOANFUNDSREQUESTED_EVENT, transactionReceipt);
        ArrayList<LoanFundsRequestedEventResponse> responses = new ArrayList<LoanFundsRequestedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoanFundsRequestedEventResponse typedResponse = new LoanFundsRequestedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.time = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static LoanFundsRequestedEventResponse getLoanFundsRequestedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LOANFUNDSREQUESTED_EVENT, log);
        LoanFundsRequestedEventResponse typedResponse = new LoanFundsRequestedEventResponse();
        typedResponse.log = log;
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.time = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<LoanFundsRequestedEventResponse> loanFundsRequestedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getLoanFundsRequestedEventFromLog(log));
    }

    public Flowable<LoanFundsRequestedEventResponse> loanFundsRequestedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOANFUNDSREQUESTED_EVENT));
        return loanFundsRequestedEventFlowable(filter);
    }

    public static List<LoanIssuedEventResponse> getLoanIssuedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LOANISSUED_EVENT, transactionReceipt);
        ArrayList<LoanIssuedEventResponse> responses = new ArrayList<LoanIssuedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoanIssuedEventResponse typedResponse = new LoanIssuedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.borrower = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.loanId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.principal = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.interest = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.totalAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.monthlyPayment = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.interestRate = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.term = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.dueTime = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static LoanIssuedEventResponse getLoanIssuedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LOANISSUED_EVENT, log);
        LoanIssuedEventResponse typedResponse = new LoanIssuedEventResponse();
        typedResponse.log = log;
        typedResponse.borrower = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.loanId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.principal = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.interest = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.totalAmount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.monthlyPayment = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.interestRate = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.term = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.dueTime = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        return typedResponse;
    }

    public Flowable<LoanIssuedEventResponse> loanIssuedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getLoanIssuedEventFromLog(log));
    }

    public Flowable<LoanIssuedEventResponse> loanIssuedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOANISSUED_EVENT));
        return loanIssuedEventFlowable(filter);
    }

    public static List<LoanRepaidEventResponse> getLoanRepaidEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(LOANREPAID_EVENT, transactionReceipt);
        ArrayList<LoanRepaidEventResponse> responses = new ArrayList<LoanRepaidEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoanRepaidEventResponse typedResponse = new LoanRepaidEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.borrower = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.loanId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amountRepaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static LoanRepaidEventResponse getLoanRepaidEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(LOANREPAID_EVENT, log);
        LoanRepaidEventResponse typedResponse = new LoanRepaidEventResponse();
        typedResponse.log = log;
        typedResponse.borrower = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.loanId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.amountRepaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<LoanRepaidEventResponse> loanRepaidEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getLoanRepaidEventFromLog(log));
    }

    public Flowable<LoanRepaidEventResponse> loanRepaidEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOANREPAID_EVENT));
        return loanRepaidEventFlowable(filter);
    }

    public static List<MonthlyPaymentMadeEventResponse> getMonthlyPaymentMadeEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MONTHLYPAYMENTMADE_EVENT, transactionReceipt);
        ArrayList<MonthlyPaymentMadeEventResponse> responses = new ArrayList<MonthlyPaymentMadeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MonthlyPaymentMadeEventResponse typedResponse = new MonthlyPaymentMadeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.borrower = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.loanId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amountPaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.totalPaid = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MonthlyPaymentMadeEventResponse getMonthlyPaymentMadeEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MONTHLYPAYMENTMADE_EVENT, log);
        MonthlyPaymentMadeEventResponse typedResponse = new MonthlyPaymentMadeEventResponse();
        typedResponse.log = log;
        typedResponse.borrower = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.loanId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.amountPaid = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.totalPaid = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<MonthlyPaymentMadeEventResponse> monthlyPaymentMadeEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMonthlyPaymentMadeEventFromLog(log));
    }

    public Flowable<MonthlyPaymentMadeEventResponse> monthlyPaymentMadeEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MONTHLYPAYMENTMADE_EVENT));
        return monthlyPaymentMadeEventFlowable(filter);
    }

    public RemoteFunctionCall<Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>> getLoanInfo(String _borrower, BigInteger _loanId) {
        final Function function = new Function(FUNC_GETLOANINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.generated.Uint32(_loanId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getTotalActiveLoanAmount() {
        final Function function = new Function(FUNC_GETTOTALACTIVELOANAMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> issueLoan(BigInteger _loanId, String _borrower, BigInteger _principal, BigInteger _interest, BigInteger _interestRate, BigInteger _term, BigInteger _loanPeriodInSeconds, BigInteger _monthlyPayment) {
        final Function function = new Function(
                FUNC_ISSUELOAN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_loanId), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.generated.Uint256(_principal), 
                new org.web3j.abi.datatypes.generated.Uint256(_interest), 
                new org.web3j.abi.datatypes.generated.Uint32(_interestRate), 
                new org.web3j.abi.datatypes.generated.Uint32(_term), 
                new org.web3j.abi.datatypes.generated.Uint256(_loanPeriodInSeconds), 
                new org.web3j.abi.datatypes.generated.Uint256(_monthlyPayment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple10<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>> loans(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_LOANS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint32(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint32>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple10<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple10<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple10<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple10<BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue(), 
                                (BigInteger) results.get(8).getValue(), 
                                (Boolean) results.get(9).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> makeMonthlyPayment(BigInteger _loanId) {
        final Function function = new Function(
                FUNC_MAKEMONTHLYPAYMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_loanId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> nhdcToken() {
        final Function function = new Function(FUNC_NHDCTOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalActiveLoanAmount() {
        final Function function = new Function(FUNC_TOTALACTIVELOANAMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static LoanContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LoanContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LoanContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LoanContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LoanContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LoanContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LoanContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LoanContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LoanContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(LoanContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<LoanContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(LoanContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LoanContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(LoanContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<LoanContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(LoanContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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

    public static class LoanFundsRequestedEventResponse extends BaseEventResponse {
        public BigInteger amount;

        public BigInteger time;
    }

    public static class LoanIssuedEventResponse extends BaseEventResponse {
        public String borrower;

        public BigInteger loanId;

        public BigInteger principal;

        public BigInteger interest;

        public BigInteger totalAmount;

        public BigInteger monthlyPayment;

        public BigInteger interestRate;

        public BigInteger term;

        public BigInteger dueTime;
    }

    public static class LoanRepaidEventResponse extends BaseEventResponse {
        public String borrower;

        public BigInteger loanId;

        public BigInteger amountRepaid;
    }

    public static class MonthlyPaymentMadeEventResponse extends BaseEventResponse {
        public String borrower;

        public BigInteger loanId;

        public BigInteger amountPaid;

        public BigInteger totalPaid;
    }
}

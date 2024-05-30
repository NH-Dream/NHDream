package com.ssafy.nhdream.domain.saving.contract;

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
 * <p>Generated with web3j version 1.5.2.
 */
@SuppressWarnings("rawtypes")
public class SavingContract extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b50604051610f9d380380610f9d83398181016040528101906100319190610114565b805f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055503360015f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505061013f565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6100e3826100ba565b9050919050565b6100f3816100d9565b81146100fd575f80fd5b50565b5f8151905061010e816100ea565b92915050565b5f60208284031215610129576101286100b6565b5b5f61013684828501610100565b91505092915050565b610e518061014c5f395ff3fe608060405234801561000f575f80fd5b5060043610610060575f3560e01c80631e347d8d14610064578063636565fc146100825780636df9fb541461009e57806378aa07db146100d15780638da5cb5b146100ed578063aaf103981461010b575b5f80fd5b61006c610127565b6040516100799190610816565b60405180910390f35b61009c6004803603810190610097919061089f565b61014a565b005b6100b860048036038101906100b3919061093e565b610348565b6040516100c894939291906109a5565b60405180910390f35b6100eb60048036038101906100e691906109e8565b61038b565b005b6100f56105d2565b6040516101029190610a22565b60405180910390f35b61012560048036038101906101209190610a3b565b6105f7565b005b5f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b5f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff1660e01b81526004016101a693929190610a79565b6020604051808303815f875af11580156101c2573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906101e69190610ad8565b610225576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161021c90610b5d565b60405180910390fd5b5f81426102329190610ba8565b905060405180608001604052808581526020018481526020018281526020015f151581525060025f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8763ffffffff1663ffffffff1681526020019081526020015f205f820151815f015560208201518160010155604082015181600201556060820151816003015f6101000a81548160ff0219169083151502179055509050503373ffffffffffffffffffffffffffffffffffffffff167f32d27694d236e981c4f4f4ad272828b6b53796c5eee4a822364a143a21bbc00d868686856040516103399493929190610bea565b60405180910390a25050505050565b6002602052815f5260405f20602052805f5260405f205f9150915050805f015490806001015490806002015490806003015f9054906101000a900460ff16905084565b5f60025f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8363ffffffff1663ffffffff1681526020019081526020015f209050806002015442101561042d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161042490610c77565b60405180910390fd5b5f1515816003015f9054906101000a900460ff16151514610483576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161047a90610cdf565b60405180910390fd5b5f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb33835f01546040518363ffffffff1660e01b81526004016104e0929190610cfd565b6020604051808303815f875af11580156104fc573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906105209190610ad8565b61055f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161055690610d6e565b60405180910390fd5b6001816003015f6101000a81548160ff0219169083151502179055503373ffffffffffffffffffffffffffffffffffffffff167fd27d07f78406b30c94affa5e0deae8dccefcc57f6d9812b6de24dbaa539c50c383835f01546040516105c6929190610d8c565b60405180910390a25050565b60015f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b5f60025f3373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020015f205f8463ffffffff1663ffffffff1681526020019081526020015f209050806003015f9054906101000a900460ff16156106a3576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161069a90610dfd565b60405180910390fd5b5f8054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330856040518463ffffffff1660e01b81526004016106ff93929190610a79565b6020604051808303815f875af115801561071b573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061073f9190610ad8565b61077e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161077590610b5d565b60405180910390fd5b81815f015f8282546107909190610ba8565b92505081905550505050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f819050919050565b5f6107de6107d96107d48461079c565b6107bb565b61079c565b9050919050565b5f6107ef826107c4565b9050919050565b5f610800826107e5565b9050919050565b610810816107f6565b82525050565b5f6020820190506108295f830184610807565b92915050565b5f80fd5b5f63ffffffff82169050919050565b61084b81610833565b8114610855575f80fd5b50565b5f8135905061086681610842565b92915050565b5f819050919050565b61087e8161086c565b8114610888575f80fd5b50565b5f8135905061089981610875565b92915050565b5f805f80608085870312156108b7576108b661082f565b5b5f6108c487828801610858565b94505060206108d58782880161088b565b93505060406108e68782880161088b565b92505060606108f78782880161088b565b91505092959194509250565b5f61090d8261079c565b9050919050565b61091d81610903565b8114610927575f80fd5b50565b5f8135905061093881610914565b92915050565b5f80604083850312156109545761095361082f565b5b5f6109618582860161092a565b925050602061097285828601610858565b9150509250929050565b6109858161086c565b82525050565b5f8115159050919050565b61099f8161098b565b82525050565b5f6080820190506109b85f83018761097c565b6109c5602083018661097c565b6109d2604083018561097c565b6109df6060830184610996565b95945050505050565b5f602082840312156109fd576109fc61082f565b5b5f610a0a84828501610858565b91505092915050565b610a1c81610903565b82525050565b5f602082019050610a355f830184610a13565b92915050565b5f8060408385031215610a5157610a5061082f565b5b5f610a5e85828601610858565b9250506020610a6f8582860161088b565b9150509250929050565b5f606082019050610a8c5f830186610a13565b610a996020830185610a13565b610aa6604083018461097c565b949350505050565b610ab78161098b565b8114610ac1575f80fd5b50565b5f81519050610ad281610aae565b92915050565b5f60208284031215610aed57610aec61082f565b5b5f610afa84828501610ac4565b91505092915050565b5f82825260208201905092915050565b7f5472616e73666572206661696c656400000000000000000000000000000000005f82015250565b5f610b47600f83610b03565b9150610b5282610b13565b602082019050919050565b5f6020820190508181035f830152610b7481610b3b565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f610bb28261086c565b9150610bbd8361086c565b9250828201905080821115610bd557610bd4610b7b565b5b92915050565b610be481610833565b82525050565b5f608082019050610bfd5f830187610bdb565b610c0a602083018661097c565b610c17604083018561097c565b610c24606083018461097c565b95945050505050565b7f4163636f756e74206e6f74206d617475726564207965740000000000000000005f82015250565b5f610c61601783610b03565b9150610c6c82610c2d565b602082019050919050565b5f6020820190508181035f830152610c8e81610c55565b9050919050565b7f4163636f756e7420616c726561647920736574746c65640000000000000000005f82015250565b5f610cc9601783610b03565b9150610cd482610c95565b602082019050919050565b5f6020820190508181035f830152610cf681610cbd565b9050919050565b5f604082019050610d105f830185610a13565b610d1d602083018461097c565b9392505050565b7f4661696c656420746f2072657475726e207072696e636970616c0000000000005f82015250565b5f610d58601a83610b03565b9150610d6382610d24565b602082019050919050565b5f6020820190508181035f830152610d8581610d4c565b9050919050565b5f604082019050610d9f5f830185610bdb565b610dac602083018461097c565b9392505050565b7f4163636f756e742068617320616c7265616479206d61747572656400000000005f82015250565b5f610de7601b83610b03565b9150610df282610db3565b602082019050919050565b5f6020820190508181035f830152610e1481610ddb565b905091905056fea2646970667358221220ebdb19d116b32d96ac14462db36705e49a5585ced1c26317e100a5c8848330d464736f6c63430008190033";

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
    protected SavingContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SavingContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SavingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SavingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<SavingContract.NewAccountEventResponse> getNewAccountEvents(TransactionReceipt transactionReceipt) {
        List<Log> logs = transactionReceipt.getLogs();
        ArrayList<SavingContract.NewAccountEventResponse> responses = new ArrayList<SavingContract.NewAccountEventResponse>();
        for (Log log : logs) {
            EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NEWACCOUNT_EVENT, log);
            if (eventValues == null) {
                continue;
            }
            SavingContract.NewAccountEventResponse typedResponse = new SavingContract.NewAccountEventResponse();
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

    public static List<SavingContract.PrincipalReturnedEventResponse> getPrincipalReturnedEvents(TransactionReceipt transactionReceipt) {
        List<Log> logs = transactionReceipt.getLogs();
        ArrayList<SavingContract.PrincipalReturnedEventResponse> responses = new ArrayList<SavingContract.PrincipalReturnedEventResponse>();
        for (Log log : logs) {
            EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PRINCIPALRETURNED_EVENT, log);
            if (eventValues == null) {
                continue;
            }
            SavingContract.PrincipalReturnedEventResponse typedResponse = new SavingContract.PrincipalReturnedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().getFirst().getValue();
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
    public static SavingContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SavingContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SavingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SavingContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SavingContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SavingContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SavingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SavingContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SavingContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(SavingContract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<SavingContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(SavingContract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SavingContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(SavingContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SavingContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _tokenAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _tokenAddress)));
        return deployRemoteCall(SavingContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
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

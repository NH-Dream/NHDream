package com.ssafy.nhdream.common.drdc;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class DRDCContract extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b506040518060400160405280600781526020017f4452546f6b656e000000000000000000000000000000000000000000000000008152506040518060400160405280600481526020017f445244430000000000000000000000000000000000000000000000000000000081525081600390816200008f91906200042a565b508060049081620000a191906200042a565b505050620000c4620000b8620000e260201b60201c565b620000ea60201b60201c565b60006006819055506000600781905550600060088190555062000511565b600033905090565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b600081519050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b600060028204905060018216806200023257607f821691505b602082108103620002485762000247620001ea565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302620002b27fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8262000273565b620002be868362000273565b95508019841693508086168417925050509392505050565b6000819050919050565b6000819050919050565b60006200030b62000305620002ff84620002d6565b620002e0565b620002d6565b9050919050565b6000819050919050565b6200032783620002ea565b6200033f620003368262000312565b84845462000280565b825550505050565b600090565b6200035662000347565b620003638184846200031c565b505050565b5b818110156200038b576200037f6000826200034c565b60018101905062000369565b5050565b601f821115620003da57620003a4816200024e565b620003af8462000263565b81016020851015620003bf578190505b620003d7620003ce8562000263565b83018262000368565b50505b505050565b600082821c905092915050565b6000620003ff60001984600802620003df565b1980831691505092915050565b60006200041a8383620003ec565b9150826002028217905092915050565b6200043582620001b0565b67ffffffffffffffff811115620004515762000450620001bb565b5b6200045d825462000219565b6200046a8282856200038f565b600060209050601f831160018114620004a257600084156200048d578287015190505b6200049985826200040c565b86555062000509565b601f198416620004b2866200024e565b60005b82811015620004dc57848901518255600182019150602085019450602081019050620004b5565b86831015620004fc5784890151620004f8601f891682620003ec565b8355505b6001600288020188555050505b505050505050565b6123be80620005216000396000f3fe608060405234801561001057600080fd5b50600436106101585760003560e01c80638da5cb5b116100c3578063aaf502921161007c578063aaf50292146103c7578063d0047acf146103e3578063d89135cd146103ff578063dd62ed3e1461041d578063e5919a4f1461044d578063f2fde38b1461047d57610158565b80638da5cb5b146102f157806395d89b411461030f5780639e0921ff1461032d578063a2309ff814610349578063a457c2d714610367578063a9059cbb1461039757610158565b8063313ce56711610115578063313ce56714610231578063395093511461024f57806342966c681461027f57806370a082311461029b578063715018a6146102cb57806379cc6790146102d557610158565b806306fdde031461015d578063095ea7b31461017b57806318160ddd146101ab57806323b872dd146101c957806326772c29146101f95780632caf94ce14610215575b600080fd5b610165610499565b6040516101729190611855565b60405180910390f35b61019560048036038101906101909190611910565b61052b565b6040516101a2919061196b565b60405180910390f35b6101b361054e565b6040516101c09190611995565b60405180910390f35b6101e360048036038101906101de91906119b0565b610558565b6040516101f0919061196b565b60405180910390f35b610213600480360381019061020e9190611a3f565b610612565b005b61022f600480360381019061022a9190611a92565b61070b565b005b610239610786565b6040516102469190611aee565b60405180910390f35b61026960048036038101906102649190611910565b61078b565b604051610276919061196b565b60405180910390f35b61029960048036038101906102949190611b09565b6107c2565b005b6102b560048036038101906102b09190611b36565b6107d6565b6040516102c29190611995565b60405180910390f35b6102d361081e565b005b6102ef60048036038101906102ea9190611910565b6108a6565b005b6102f9610921565b6040516103069190611b72565b60405180910390f35b61031761094b565b6040516103249190611855565b60405180910390f35b61034760048036038101906103429190611b36565b6109dd565b005b610351610ab4565b60405161035e9190611995565b60405180910390f35b610381600480360381019061037c9190611910565b610abe565b60405161038e919061196b565b60405180910390f35b6103b160048036038101906103ac9190611910565b610b35565b6040516103be919061196b565b60405180910390f35b6103e160048036038101906103dc9190611b36565b610b58565b005b6103fd60048036038101906103f89190611a3f565b610c2f565b005b610407610d28565b6040516104149190611995565b60405180910390f35b61043760048036038101906104329190611b8d565b610d32565b6040516104449190611995565b60405180910390f35b61046760048036038101906104629190611b36565b610db9565b604051610474919061196b565b60405180910390f35b61049760048036038101906104929190611b36565b610dd9565b005b6060600380546104a890611bfc565b80601f01602080910402602001604051908101604052809291908181526020018280546104d490611bfc565b80156105215780601f106104f657610100808354040283529160200191610521565b820191906000526020600020905b81548152906001019060200180831161050457829003601f168201915b5050505050905090565b600080610536610ed0565b9050610543818585610ed8565b600191505092915050565b6000600254905090565b6000600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16806105e457503373ffffffffffffffffffffffffffffffffffffffff166105cc610921565b73ffffffffffffffffffffffffffffffffffffffff16145b156105fd576105f48484846110a1565b6001905061060b565b610608848484611317565b90505b9392505050565b61061a610ed0565b73ffffffffffffffffffffffffffffffffffffffff16610638610921565b73ffffffffffffffffffffffffffffffffffffffff161461068e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161068590611c79565b60405180910390fd5b61069883836108a6565b81600760008282546106aa9190611cc8565b925050819055508063ffffffff168373ffffffffffffffffffffffffffffffffffffffff167ff9456e24470a717eda4995a1149f46e243fc7835b18040cf004b810e30ccee8b846040516106fe9190611995565b60405180910390a3505050565b610714826107c2565b81600760008282546107269190611cc8565b925050819055508063ffffffff163373ffffffffffffffffffffffffffffffffffffffff167ff9456e24470a717eda4995a1149f46e243fc7835b18040cf004b810e30ccee8b8460405161077a9190611995565b60405180910390a35050565b600090565b600080610796610ed0565b90506107b78185856107a88589610d32565b6107b29190611cc8565b610ed8565b600191505092915050565b6107d36107cd610ed0565b82611346565b50565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b610826610ed0565b73ffffffffffffffffffffffffffffffffffffffff16610844610921565b73ffffffffffffffffffffffffffffffffffffffff161461089a576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161089190611c79565b60405180910390fd5b6108a46000611513565b565b60006108b9836108b4610ed0565b610d32565b9050818110156108fe576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108f590611d6e565b60405180910390fd5b6109128361090a610ed0565b848403610ed8565b61091c8383611346565b505050565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60606004805461095a90611bfc565b80601f016020809104026020016040519081016040528092919081815260200182805461098690611bfc565b80156109d35780601f106109a8576101008083540402835291602001916109d3565b820191906000526020600020905b8154815290600101906020018083116109b657829003601f168201915b5050505050905090565b6109e5610ed0565b73ffffffffffffffffffffffffffffffffffffffff16610a03610921565b73ffffffffffffffffffffffffffffffffffffffff1614610a59576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610a5090611c79565b60405180910390fd5b6000600960008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b6000600654905090565b600080610ac9610ed0565b90506000610ad78286610d32565b905083811015610b1c576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610b1390611e00565b60405180910390fd5b610b298286868403610ed8565b60019250505092915050565b600080610b40610ed0565b9050610b4d8185856110a1565b600191505092915050565b610b60610ed0565b73ffffffffffffffffffffffffffffffffffffffff16610b7e610921565b73ffffffffffffffffffffffffffffffffffffffff1614610bd4576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610bcb90611c79565b60405180910390fd5b6001600960008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555050565b610c37610ed0565b73ffffffffffffffffffffffffffffffffffffffff16610c55610921565b73ffffffffffffffffffffffffffffffffffffffff1614610cab576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ca290611c79565b60405180910390fd5b610cb583836115d9565b8160066000828254610cc79190611cc8565b925050819055508063ffffffff168373ffffffffffffffffffffffffffffffffffffffff167f5c7e09fa4615060d4b3303502bd5d8f0f030010aa720da4b454ad3672e5645f484604051610d1b9190611995565b60405180910390a3505050565b6000600754905090565b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b60096020528060005260406000206000915054906101000a900460ff1681565b610de1610ed0565b73ffffffffffffffffffffffffffffffffffffffff16610dff610921565b73ffffffffffffffffffffffffffffffffffffffff1614610e55576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610e4c90611c79565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603610ec4576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610ebb90611e92565b60405180910390fd5b610ecd81611513565b50565b600033905090565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603610f47576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610f3e90611f24565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603610fb6576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610fad90611fb6565b60405180910390fd5b80600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925836040516110949190611995565b60405180910390a3505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603611110576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161110790612048565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff160361117f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401611176906120da565b60405180910390fd5b61118a83838361172f565b60008060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905081811015611210576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016112079061216c565b60405180910390fd5b8181036000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040516112fe9190611995565b60405180910390a3611311848484611734565b50505050565b600080611322610ed0565b905061132f858285611739565b61133a8585856110a1565b60019150509392505050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16036113b5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016113ac906121fe565b60405180910390fd5b6113c18260008361172f565b60008060008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905081811015611447576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161143e90612290565b60405180910390fd5b8181036000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555081600260008282540392505081905550600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040516114fa9190611995565b60405180910390a361150e83600084611734565b505050565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603611648576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161163f906122fc565b60405180910390fd5b6116546000838361172f565b80600260008282546116669190611cc8565b92505081905550806000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040516117179190611995565b60405180910390a361172b60008383611734565b5050565b505050565b505050565b60006117458484610d32565b90507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff81146117bf57818110156117b1576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016117a890612368565b60405180910390fd5b6117be8484848403610ed8565b5b50505050565b600081519050919050565b600082825260208201905092915050565b60005b838110156117ff5780820151818401526020810190506117e4565b60008484015250505050565b6000601f19601f8301169050919050565b6000611827826117c5565b61183181856117d0565b93506118418185602086016117e1565b61184a8161180b565b840191505092915050565b6000602082019050818103600083015261186f818461181c565b905092915050565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006118a78261187c565b9050919050565b6118b78161189c565b81146118c257600080fd5b50565b6000813590506118d4816118ae565b92915050565b6000819050919050565b6118ed816118da565b81146118f857600080fd5b50565b60008135905061190a816118e4565b92915050565b6000806040838503121561192757611926611877565b5b6000611935858286016118c5565b9250506020611946858286016118fb565b9150509250929050565b60008115159050919050565b61196581611950565b82525050565b6000602082019050611980600083018461195c565b92915050565b61198f816118da565b82525050565b60006020820190506119aa6000830184611986565b92915050565b6000806000606084860312156119c9576119c8611877565b5b60006119d7868287016118c5565b93505060206119e8868287016118c5565b92505060406119f9868287016118fb565b9150509250925092565b600063ffffffff82169050919050565b611a1c81611a03565b8114611a2757600080fd5b50565b600081359050611a3981611a13565b92915050565b600080600060608486031215611a5857611a57611877565b5b6000611a66868287016118c5565b9350506020611a77868287016118fb565b9250506040611a8886828701611a2a565b9150509250925092565b60008060408385031215611aa957611aa8611877565b5b6000611ab7858286016118fb565b9250506020611ac885828601611a2a565b9150509250929050565b600060ff82169050919050565b611ae881611ad2565b82525050565b6000602082019050611b036000830184611adf565b92915050565b600060208284031215611b1f57611b1e611877565b5b6000611b2d848285016118fb565b91505092915050565b600060208284031215611b4c57611b4b611877565b5b6000611b5a848285016118c5565b91505092915050565b611b6c8161189c565b82525050565b6000602082019050611b876000830184611b63565b92915050565b60008060408385031215611ba457611ba3611877565b5b6000611bb2858286016118c5565b9250506020611bc3858286016118c5565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680611c1457607f821691505b602082108103611c2757611c26611bcd565b5b50919050565b7f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e6572600082015250565b6000611c636020836117d0565b9150611c6e82611c2d565b602082019050919050565b60006020820190508181036000830152611c9281611c56565b9050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000611cd3826118da565b9150611cde836118da565b9250828201905080821115611cf657611cf5611c99565b5b92915050565b7f45524332303a206275726e20616d6f756e74206578636565647320616c6c6f7760008201527f616e636500000000000000000000000000000000000000000000000000000000602082015250565b6000611d586024836117d0565b9150611d6382611cfc565b604082019050919050565b60006020820190508181036000830152611d8781611d4b565b9050919050565b7f45524332303a2064656372656173656420616c6c6f77616e63652062656c6f7760008201527f207a65726f000000000000000000000000000000000000000000000000000000602082015250565b6000611dea6025836117d0565b9150611df582611d8e565b604082019050919050565b60006020820190508181036000830152611e1981611ddd565b9050919050565b7f4f776e61626c653a206e6577206f776e657220697320746865207a65726f206160008201527f6464726573730000000000000000000000000000000000000000000000000000602082015250565b6000611e7c6026836117d0565b9150611e8782611e20565b604082019050919050565b60006020820190508181036000830152611eab81611e6f565b9050919050565b7f45524332303a20617070726f76652066726f6d20746865207a65726f2061646460008201527f7265737300000000000000000000000000000000000000000000000000000000602082015250565b6000611f0e6024836117d0565b9150611f1982611eb2565b604082019050919050565b60006020820190508181036000830152611f3d81611f01565b9050919050565b7f45524332303a20617070726f766520746f20746865207a65726f20616464726560008201527f7373000000000000000000000000000000000000000000000000000000000000602082015250565b6000611fa06022836117d0565b9150611fab82611f44565b604082019050919050565b60006020820190508181036000830152611fcf81611f93565b9050919050565b7f45524332303a207472616e736665722066726f6d20746865207a65726f20616460008201527f6472657373000000000000000000000000000000000000000000000000000000602082015250565b60006120326025836117d0565b915061203d82611fd6565b604082019050919050565b6000602082019050818103600083015261206181612025565b9050919050565b7f45524332303a207472616e7366657220746f20746865207a65726f206164647260008201527f6573730000000000000000000000000000000000000000000000000000000000602082015250565b60006120c46023836117d0565b91506120cf82612068565b604082019050919050565b600060208201905081810360008301526120f3816120b7565b9050919050565b7f45524332303a207472616e7366657220616d6f756e742065786365656473206260008201527f616c616e63650000000000000000000000000000000000000000000000000000602082015250565b60006121566026836117d0565b9150612161826120fa565b604082019050919050565b6000602082019050818103600083015261218581612149565b9050919050565b7f45524332303a206275726e2066726f6d20746865207a65726f2061646472657360008201527f7300000000000000000000000000000000000000000000000000000000000000602082015250565b60006121e86021836117d0565b91506121f38261218c565b604082019050919050565b60006020820190508181036000830152612217816121db565b9050919050565b7f45524332303a206275726e20616d6f756e7420657863656564732062616c616e60008201527f6365000000000000000000000000000000000000000000000000000000000000602082015250565b600061227a6022836117d0565b91506122858261221e565b604082019050919050565b600060208201905081810360008301526122a98161226d565b9050919050565b7f45524332303a206d696e7420746f20746865207a65726f206164647265737300600082015250565b60006122e6601f836117d0565b91506122f1826122b0565b602082019050919050565b60006020820190508181036000830152612315816122d9565b9050919050565b7f45524332303a20696e73756666696369656e7420616c6c6f77616e6365000000600082015250565b6000612352601d836117d0565b915061235d8261231c565b602082019050919050565b6000602082019050818103600083015261238181612345565b905091905056fea2646970667358221220e4eb4b17ed724e726046123a7207d2b261ca01bfd2e6fd1d65a15ce11431c69064736f6c63430008130033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_BURNFROM = "burnFrom";

    public static final String FUNC_BURNFROMWITHTIME = "burnFromWithTime";

    public static final String FUNC_BURNWITHTIME = "burnWithTime";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_DECREASEALLOWANCE = "decreaseAllowance";

    public static final String FUNC_DELETETRUSTEDCONTRACT = "deleteTrustedContract";

    public static final String FUNC_INCREASEALLOWANCE = "increaseAllowance";

    public static final String FUNC_MINT = "mint";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETTRUSTEDCONTRACT = "setTrustedContract";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALBURNED = "totalBurned";

    public static final String FUNC_TOTALMINTED = "totalMinted";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_TRUSTEDCONTRACTS = "trustedContracts";

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BURNED_EVENT = new Event("Burned",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint32>(true) {}));
    ;

    public static final Event DRDCTRANSFERRED_EVENT = new Event("DRDCTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint32>(true) {}));
    ;

    public static final Event MINTED_EVENT = new Event("Minted",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint32>(true) {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected DRDCContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DRDCContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DRDCContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DRDCContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovalEventResponse getApprovalEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVAL_EVENT, log);
        ApprovalEventResponse typedResponse = new ApprovalEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovalEventFromLog(log));
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    public static List<BurnedEventResponse> getBurnedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(BURNED_EVENT, transactionReceipt);
        ArrayList<BurnedEventResponse> responses = new ArrayList<BurnedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BurnedEventResponse typedResponse = new BurnedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.time = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BurnedEventResponse getBurnedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BURNED_EVENT, log);
        BurnedEventResponse typedResponse = new BurnedEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.time = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<BurnedEventResponse> burnedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBurnedEventFromLog(log));
    }

    public Flowable<BurnedEventResponse> burnedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, int targetDate) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BURNED_EVENT));
        filter.addOptionalTopics(null, targetDate + "");
        return burnedEventFlowable(filter);
    }

    public static List<DRDCTransferredEventResponse> getDRDCTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DRDCTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<DRDCTransferredEventResponse> responses = new ArrayList<DRDCTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DRDCTransferredEventResponse typedResponse = new DRDCTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.time = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DRDCTransferredEventResponse getDRDCTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DRDCTRANSFERRED_EVENT, log);
        DRDCTransferredEventResponse typedResponse = new DRDCTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.time = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<DRDCTransferredEventResponse> dRDCTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDRDCTransferredEventFromLog(log));
    }

    public Flowable<DRDCTransferredEventResponse> dRDCTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DRDCTRANSFERRED_EVENT));
        return dRDCTransferredEventFlowable(filter);
    }

    public static List<MintedEventResponse> getMintedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(MINTED_EVENT, transactionReceipt);
        ArrayList<MintedEventResponse> responses = new ArrayList<MintedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            MintedEventResponse typedResponse = new MintedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.time = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static MintedEventResponse getMintedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(MINTED_EVENT, log);
        MintedEventResponse typedResponse = new MintedEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.time = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<MintedEventResponse> mintedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getMintedEventFromLog(log));
    }

    public Flowable<MintedEventResponse> mintedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, int targetDate) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MINTED_EVENT));
        filter.addOptionalTopics(null, targetDate + "");
        return mintedEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static OwnershipTransferredEventResponse getOwnershipTransferredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
        OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getOwnershipTransferredEventFromLog(log));
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public static List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferEventFromLog(log));
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner),
                        new org.web3j.abi.datatypes.Address(160, spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger amount) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> burn(BigInteger amount) {
        final Function function = new Function(
                FUNC_BURN,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnFrom(String account, BigInteger amount) {
        final Function function = new Function(
                FUNC_BURNFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnFromWithTime(String account, BigInteger amount, BigInteger time) {
        final Function function = new Function(
                FUNC_BURNFROMWITHTIME,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(amount),
                        new org.web3j.abi.datatypes.generated.Uint32(time)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnWithTime(BigInteger amount, BigInteger time) {
        final Function function = new Function(
                FUNC_BURNWITHTIME,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(amount),
                        new org.web3j.abi.datatypes.generated.Uint32(time)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> decreaseAllowance(String spender, BigInteger subtractedValue) {
        final Function function = new Function(
                FUNC_DECREASEALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(subtractedValue)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteTrustedContract(String _contract) {
        final Function function = new Function(
                FUNC_DELETETRUSTEDCONTRACT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _contract)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> increaseAllowance(String spender, BigInteger addedValue) {
        final Function function = new Function(
                FUNC_INCREASEALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(addedValue)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> mint(String to, BigInteger amount, BigInteger time) {
        final Function function = new Function(
                FUNC_MINT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(amount),
                        new org.web3j.abi.datatypes.generated.Uint32(time)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final Function function = new Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setTrustedContract(String _contract) {
        final Function function = new Function(
                FUNC_SETTRUSTEDCONTRACT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _contract)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalBurned() {
        final Function function = new Function(FUNC_TOTALBURNED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> totalMinted() {
        final Function function = new Function(FUNC_TOTALMINTED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger amount) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String sender, String recipient, BigInteger amount) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, sender),
                        new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> trustedContracts(String param0) {
        final Function function = new Function(FUNC_TRUSTEDCONTRACTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static DRDCContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DRDCContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DRDCContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DRDCContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DRDCContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DRDCContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DRDCContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DRDCContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DRDCContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DRDCContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<DRDCContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DRDCContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<DRDCContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DRDCContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<DRDCContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DRDCContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class BurnedEventResponse extends BaseEventResponse {
        public String from;

        public BigInteger time;

        public BigInteger amount;
    }

    public static class DRDCTransferredEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger time;

        public BigInteger amount;
    }

    public static class MintedEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger time;

        public BigInteger amount;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}

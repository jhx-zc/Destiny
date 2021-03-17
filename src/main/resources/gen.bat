protoc --proto_path=. --java_out=../java rpc.proto

pbjs -t static-module -w commonjs -o rpc.js rpc.proto && pbts -o rpc.d.ts rpc.js
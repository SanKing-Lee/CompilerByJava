extern int a = 0;

int main(){
    func(3);
    return 0;
}

int func(int b){
    int *ptr = &a;
    int c = 3.2;
    int d = b+c*a;
    int i;
    switch(a){
        case 0: c++; break;
        case 1: c--;break;
        default: c= 0; break;
    }
    for(i = 0; i < 10; i++){
        a++;
    }
    while(a > 0){
        a--;
    }
    if(b != c){
        return a;
    }
    else{
        return b;
    }
}}
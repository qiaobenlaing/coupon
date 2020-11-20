//
//  BMSQ_StaffUpdate.m
//  BMSQS
//
//  Created by gh on 15/11/3.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_StaffUpdate.h"
#import "SVProgressHUD.h"


@interface BMSQ_StaffUpdate(){
    
    UITextField *text_top;
    UITextField *text_bottom;
    
    
    
}
@end

@implementation BMSQ_StaffUpdate

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
}

- (void)setViewUp {
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self setNavBackItem];
    [self setNavRitItem];
    
    
    CGFloat x=20;
    CGFloat y=10;
    CGFloat height = 40;
    CGFloat width =APP_VIEW_WIDTH-2*x;
    
    CGFloat lbFont = 13;
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 5)];
    lineView.backgroundColor = [ UIColor whiteColor];
    [self.view addSubview:lineView];
    

    text_top = [[UITextField alloc] initWithFrame:CGRectMake(x, APP_VIEW_ORIGIN_Y+y, width, height)];
    text_top.text = @"";
    text_top.backgroundColor = [UIColor whiteColor];
    text_top.font = [UIFont systemFontOfSize:lbFont];
    [self.view addSubview:text_top];
    

    text_bottom = [[UITextField alloc] initWithFrame:CGRectMake(x,  APP_VIEW_ORIGIN_Y+height+2*y, width, height)];
    text_bottom.text = @"";
    text_bottom.backgroundColor = [UIColor whiteColor];
    text_bottom.delegate = self;
    text_bottom.font = [UIFont systemFontOfSize:lbFont];
    text_bottom.keyboardType = UIKeyboardTypeNumberPad;
    [self.view addSubview:text_bottom];
    
    if (_staffDic) {
        text_top.text = [_staffDic objectForKey:@"realName"];
        text_bottom.text = [_staffDic objectForKey:@"mobileNbr"];
    }
    
    
    if (_isUpdate) {
        [self setNavTitle:@"编辑员工信息"];
        
        UIButton *btn_delStaff = [[UIButton alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/4, 200, APP_VIEW_WIDTH/2, 44)];
        btn_delStaff.backgroundColor = [UIColor grayColor];
        btn_delStaff.tag = 903;
        [btn_delStaff addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        [btn_delStaff setTitle:@"删除" forState:UIControlStateNormal];
        [self.view addSubview:btn_delStaff];
    
        
    }else if(!_isUpdate) {
        [self setNavTitle:@"添加员工"];
        
    }
}

//保存按钮
- (void)setNavRitItem {
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH-44, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    [btnback setTitle:@"保存" forState:UIControlStateNormal];
    btnback.tag = 902;
    [btnback addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnback];
}

//退出按钮
- (void)setNavBackItem {
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    btnback.tag = 901;
    [btnback addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnback];

}

- (void)btnAction:(UIButton *)sender {

    if (sender.tag == 901) {//退出
        [self dismissViewControllerAnimated:YES completion:^{
            
        }];

    }else if (sender.tag == 902) {//保存
        
        if (_isUpdate) {
            [self updateStaff];//修改
            
        }else {
            [self addStaff];//添加
            
        }
        
        
    }else if (sender.tag == 903) {//删除
        [self delStaff];
        
    }
}



#pragma mark - request

- (void)delStaff {
    NSLog(@"删除");
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[_staffDic objectForKey:@"staffCode"] forKey:@"staffCode"];
    
    NSString* vcode = [gloabFunction getSign:@"delStaff" strParams:[_staffDic objectForKey:@"staffCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"delStaff" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        NSLog(@"%@",responseObject);
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000){
            [self dismissViewControllerAnimated:YES completion:^{
                
            }];
            
        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    

    
    

    
}

//8.33	添加，修改店员0.2
//l	方法名称：editStaffB
//l	描述：添加，修改店员
//l	令牌认证：需要
//l	输入属性：
//序号	属性	代码	数据格式	必填	值域备注
//1	店员编码	staffCode	A36	N	添加时，送空字符
//1	店员手机号	mobileNbr	N11	Y
//2	店员姓名	realName	VA50	Y
//3	店员类型	userLvl	N1	Y	1-员工；2-店长；
//4	商店编码	shopCode	A36	Y	添加店长时，送空字符
//5	父店员编码	parentCode	A36	Y	填登录的店员编码
//l	输出属性：
//序号	属性	代码	数据格式	必填	值域备注
//1	是否添加成功	code	N5	Y	50000-成功；
//20000-失败，请重试；
//60000-请输入手机号码；
//60001-手机号码不正确；
//60003-手机号已经被使用；
//80040-请输入员工姓名；
//80041-请输入员工类型；
//80043-该手机号码已被使用；
//2	新添加的店员编码	staffCode	A36	Y	若添加失败，为空字符串

- (void)updateStaff {//修改员工
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[_staffDic objectForKey:@"staffCode"] forKey:@"staffCode"];
    [params setObject:text_bottom.text forKey:@"mobileNbr"];
    [params setObject:text_top.text forKey:@"realName"];
    [params setObject:@"1" forKey:@"userLvl"];
    [params setObject:_stShopCode forKey:@"shopCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"parentCode"];
    
    NSString* vcode = [gloabFunction getSign:@"editStaffB" strParams:[_staffDic objectForKey:@"staffCode"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    [self.jsonPrcClient invokeMethod:@"editStaffB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000){
            [self dismissViewControllerAnimated:YES completion:^{
                
            }];
        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
            
        }else if (code.intValue == 60000) {
            CSAlert(@"请输入手机号码");
            
        }else if (code.intValue == 60001) {
            CSAlert(@"手机号码不正确");
            
        }else if (code.intValue == 60003){
            CSAlert(@"手机号已经被使用");
            
        }else if (code.intValue == 80040) {
            CSAlert(@"请输入员工姓名");
            
        }else if (code.intValue == 80041) {
            CSAlert(@"请输入员工类型");
            
        }else if (code.intValue == 80043) {
            CSAlert(@"该手机号码已被使用");
            
        }
        
        NSLog(@"%@",responseObject);
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];
    
    

    
}

- (void)addStaff {//添加员工
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:@"" forKey:@"staffCode"];
    [params setObject:text_bottom.text forKey:@"mobileNbr"];
    [params setObject:text_top.text forKey:@"realName"];
    [params setObject:@"1" forKey:@"userLvl"];
    [params setObject:_stShopCode forKey:@"shopCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"parentCode"];
    
    NSString* vcode = [gloabFunction getSign:@"editStaffB" strParams:@""];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    [self.jsonPrcClient invokeMethod:@"editStaffB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
         NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000){
            [self dismissViewControllerAnimated:YES completion:^{
            
            }];
        }else if (code.intValue == 20000) {
            CSAlert(@"失败，请重试");
        
        }else if (code.intValue == 60000) {
            CSAlert(@"请输入手机号码");
            
        }else if (code.intValue == 60001) {
            CSAlert(@"手机号码不正确");
            
        }else if (code.intValue == 60003){
            CSAlert(@"手机号已经被使用");
            
        }else if (code.intValue == 80040) {
            CSAlert(@"请输入员工姓名");
            
        }else if (code.intValue == 80041) {
            CSAlert(@"请输入员工类型");
            
        }else if (code.intValue == 80043) {
            CSAlert(@"该手机号码已被使用");
            
        }
        
        
        NSLog(@"%@",responseObject);
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
    }];

}


#pragma mark - UITextFiled delegate 
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    
    NSString * toBeString = [textField.text stringByReplacingCharactersInRange:range withString:string];
    
    if (toBeString.length > 11 && range.length!=1){
        textField.text = [toBeString substringToIndex:11];
        return NO;
        
    }
    return YES;
}


@end

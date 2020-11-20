//
//  AddStatffViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/31.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "AddStatffViewController.h"
#import "AddShopViewController.h"


@interface AddStatffViewController ()<UITextFieldDelegate>

@end

@implementation AddStatffViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavTitle:self.navTitleStr];
    [self setNavBackItem];
    
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [btnback setTitle:@"保存" forState:UIControlStateNormal];
    [btnback setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [btnback addTarget:self action:@selector(clickSave) forControlEvents:UIControlEventTouchUpInside];

    [self setNavRightBarItem:btnback];
    
//    UIBarButtonItem *backButtonItem = [[UIBarButtonItem alloc] initWithCustomView:btnback];
//    self.navigationItem.rightBarButtonItem = backButtonItem;
    
    UIView *bgView =[[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 81)];
    bgView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:bgView];

    [BaomiViewUtils addLine:self.view y:APP_VIEW_ORIGIN_Y color:[UIColor colorWithRed:231/255.0 green:231/255.0 blue:231/255.0 alpha:1]];
     [BaomiViewUtils addLine:bgView y:40 color:[UIColor colorWithRed:231/255.0 green:231/255.0 blue:231/255.0 alpha:1] ];
//     [BaomiViewUtils addLine:self.view y:81+15 color:[UIColor colorWithRed:231/255.0 green:231/255.0 blue:231/255.0 alpha:1]];

    
    
    
    
    UITextField *nameText = [BaomiViewUtils creatTextField:CGRectMake(10, 0, APP_VIEW_WIDTH+10, 40) bgColor:[UIColor whiteColor] borderColor:[UIColor clearColor] borderWidth:0 tag:100 cornerRadius:0 placeholder:@"姓名" font:[UIFont systemFontOfSize:13.f]];
    
    nameText.returnKeyType = UIReturnKeyDone;
    nameText.text = self.realName.length>0?[NSString stringWithFormat:@"%@",self.realName] :nil;
    nameText.delegate = self;
    [bgView addSubview:nameText];
    
    UITextField *phoneText = [BaomiViewUtils creatTextField:CGRectMake(10, 41, APP_VIEW_WIDTH+10, 40) bgColor:[UIColor whiteColor] borderColor:[UIColor clearColor] borderWidth:0 tag:200 cornerRadius:0 placeholder:@"输入手机号" font:[UIFont systemFontOfSize:13.f]];
    phoneText.text = self.mobileNbr.length>0?[NSString stringWithFormat:@"%@",self.mobileNbr] :nil;
    phoneText.keyboardType = UIKeyboardTypeNumberPad;
    phoneText.returnKeyType = UIReturnKeyDone;
    phoneText.delegate = self;
    [bgView addSubview:phoneText];
    
    [nameText addTarget:self action:@selector(chageText:) forControlEvents:UIControlEventEditingChanged];
    
    [phoneText addTarget:self action:@selector(chageText:) forControlEvents:UIControlEventEditingChanged];

}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

-(void)chageText:(UITextField *)textfied{
    
    if (textfied.tag == 100) {
        
        if (textfied.text.length>6) {
            textfied.text = [textfied.text substringToIndex:6];
        }
        self.realName = textfied.text;
        
    }else if (textfied.tag == 200){
        
        if (textfied.text.length>11) {
            textfied.text = [textfied.text substringToIndex:11];
        }
        self.mobileNbr = textfied.text;
    }
    
    
}

-(void)clickSave{

    if (self.realName == nil || self.mobileNbr==nil) {
        CSAlert(@"请填写完整");
        return;
    }
    
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:self.staffCode.length>0?self.staffCode:@"" forKey:@"staffCode"]; //修改店长
    [params setObject:self.mobileNbr forKey:@"mobileNbr"];
    [params setObject:self.realName forKey:@"realName"];
    [params setObject:self.userLvl.length>0?self.userLvl:@"2" forKey:@"userLvl"]; //2 店长
    [params setObject:[gloabFunction getStaffCode] forKey:@"parentCode"]; //空
    [params setObject:@"" forKey:@"shopCode"];                          //空



    NSString* vcode = [gloabFunction getSign:@"editStaffB" strParams:self.staffCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"editStaffB" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        if ([responseObject objectForKey:@"code"]) {
            int i = [[responseObject objectForKey:@"code"]intValue];
            if (i == 50000){
                [[NSNotificationCenter defaultCenter]postNotificationName:@"editStaff" object:nil];
                [weakSelf.navigationController popViewControllerAnimated:YES];
            }else{
                [SVProgressHUD showErrorWithStatus:@"error"];
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
    
    
}

@end

//
//  BMSQ_NewBuiltAlbumViewController.m
//  BMSQS
//
//  Created by gh on 15/10/16.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_NewBuiltAlbumViewController.h"

@interface BMSQ_NewBuiltAlbumViewController () {
    
    UITextField* text;
    
}

@end

@implementation BMSQ_NewBuiltAlbumViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setNavBackItem];
    if (_isEdit) {
        [self setNavTitle:@"修改相册名称"];
        
    }else{
        [self setNavTitle:@"新建相册"];
        
    }
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
    [self customRightBtn];
    
    [self setViewUp];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setViewUp {
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH-40, 44)];
    label.text = @"相册名称";
    [self.view addSubview:label];
    
    text = [[UITextField alloc] initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y+44+6, 300, 30)];
    
    text.borderStyle = UITextBorderStyleRoundedRect;
    
    text.autocorrectionType = UITextAutocorrectionTypeYes;
    
    text.placeholder = @"请输入相册名称";
    if (_isEdit) {
        text.text = [NSString stringWithFormat:@"%@",[_albumDic objectForKey:@"name"]];
    }
    
    
    text.returnKeyType = UIReturnKeyDone;
    
    text.clearButtonMode = UITextFieldViewModeWhileEditing;
    
    [text setBackgroundColor:[UIColor whiteColor]];
    
    text.delegate = self;
    
    [self.view addSubview:text];
    
    
}

- (void)customRightBtn
{
    
    //    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@""] style:UIBarButtonItemStylePlain target:self  action:@selector(didClickRightButton:) ];
    
//    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"完成" style:UIBarButtonItemStylePlain target:self action:@selector(didClickRightButton:) ];
//    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
//    self.navigationItem.rightBarButtonItem = item;
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(APP_VIEW_WIDTH - 64, 20, 64, 44);
    [button setTitle:@"完成" forState:UIControlStateNormal];
    [button addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:button];
    
    
    
}

- (void)didClickRightButton:(UIBarButtonItem *)item {
   
    if (text.text.length>0) { //添加新子相册
        if (_isEdit) {
            [self updateSubAlbum];
            
        }else {
            [self addSubAlbum];
            
        }
        
    }else if (text.text.length ==0) {
        UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"提示" message:@"新相册名不能为空" delegate:self cancelButtonTitle:@"确定" otherButtonTitles: nil];
        alertView.tag = 1000;
        alertView.delegate =self;
        [alertView show];
        
    }
    
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    
    return YES;
    
}

#pragma mark - 数据请求
//添加子相册
- (void)addSubAlbum {
    [SVProgressHUD showWithStatus:@"正在创建"];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:text.text forKey:@"name"];
    
    NSString* vcode = [gloabFunction getSign:@"addSubAlbum" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];

    [self.jsonPrcClient invokeMethod:@"addSubAlbum" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSNumber *code = [responseObject objectForKey:@"code"];
        switch (code.intValue) {
            case 50000:{//成功
                [SVProgressHUD showSuccessWithStatus:@"添加成功"];
                [self.navigationController popViewControllerAnimated:YES];
            }
                break;
            case 20000: {//失败
                [SVProgressHUD showSuccessWithStatus:@"添加失败"];
            }
                break;
            case 80500: {
                [SVProgressHUD showSuccessWithStatus:@"子相册名字错误"];
            }
                break;
            case 80501:{
                [SVProgressHUD showSuccessWithStatus:@"子相册名字重复"];
            }
                break;
            case 80317: {
                [SVProgressHUD showSuccessWithStatus:@"商家编码为空"];
            }
                break;
            default:
                break;
        }
        
        [SVProgressHUD dismiss];

        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
        
    }];

}

- (void)updateSubAlbum {
    
    [SVProgressHUD showWithStatus:@"正在创建"];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[self.albumDic objectForKey:@"code"] forKey:@"code"];
    [params setObject:text.text forKey:@"name"];
    
    NSString* vcode = [gloabFunction getSign:@"updateSubAlbum" strParams:[self.albumDic objectForKey:@"code"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [self.jsonPrcClient invokeMethod:@"updateSubAlbum" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSNumber *code = [responseObject objectForKey:@"code"];
        switch (code.intValue) {
            case 50000:{//成功
                [SVProgressHUD showSuccessWithStatus:@"修改成功"];
                [self.navigationController popViewControllerAnimated:YES];
            }
                break;
            case 20000: {//失败
                [SVProgressHUD showSuccessWithStatus:@"修改失败"];
            }
                break;
            case 80500: {
                [SVProgressHUD showSuccessWithStatus:@"子相册名字错误"];
            }
                break;
            case 80501:{
                [SVProgressHUD showSuccessWithStatus:@"子相册名字重复"];
            }
                break;
            case 80317: {
                [SVProgressHUD showSuccessWithStatus:@"商家编码为空"];
            }
                break;
            default:
                break;
        }
        
        [SVProgressHUD dismiss];
        
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
        
    }];
    
    
}

@end

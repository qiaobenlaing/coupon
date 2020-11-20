//
//  BMSQ_StaffAddViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/4.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_StaffAddViewController.h"

@interface BMSQ_StaffAddViewController ()

@property (weak, nonatomic) IBOutlet UIButton *checkButton1;
@property (weak, nonatomic) IBOutlet UIButton *checkButton2;
@property (weak, nonatomic) IBOutlet UITextField *mobileTextField;
@property (weak, nonatomic) IBOutlet UITextField *nameTextField;
@end

@implementation BMSQ_StaffAddViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSString *title = @"添加员工";
    
    if (_stuffInfoDic) {
        title = @"修改资料";
        _mobileTextField.text = _stuffInfoDic[@"mobileNbr"];
        _nameTextField.text   = _stuffInfoDic[@"realName"];
        
        if ([_stuffInfoDic[@"type"] integerValue] == 1) {
            [_checkButton1 setImage:[UIImage imageNamed:@"选中"] forState:UIControlStateNormal];
            _checkButton1.selected = YES;
            
        }else{
            [_checkButton2 setImage:[UIImage imageNamed:@"选中"] forState:UIControlStateNormal];
            _checkButton2.selected = YES;
            
        }
    }
    
//    [self setNavTitle:title];
//    [self setNavBackItem];
//    
//    UIButton *_rightButton = [UIButton buttonWithType:UIButtonTypeCustom];
//    _rightButton.frame = CGRectMake(self.navigationView.frame.size.width -50, 20, 40, 44);
//    [_rightButton setTitle:@"提交" forState:UIControlStateNormal];
//    _rightButton.titleLabel.font = [UIFont systemFontOfSize:15];
//    _rightButton.titleLabel.textAlignment = NSTextAlignmentRight;
//    [_rightButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//    [_rightButton addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
//    [self setNavRightBarItem:_rightButton];
    [self.navigationItem setTitle:title];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self customRightBtn];
}

- (void)customRightBtn
{
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"提交" style:UIBarButtonItemStylePlain target:self action:@selector(didClickRightButton:) ];
    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = item;
    
}

- (void)didClickRightButton:(id)sender {

    if (_stuffInfoDic) {
        [self updateStaff];
    }else{
        [self addStaff];
    }
    
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)didClickCheckBoxButton:(UIButton *)sender {
    
    sender.highlighted = YES;
    sender.selected = YES;
    
    if (sender.tag == 0) {
       
        _checkButton2.selected = NO;
        _checkButton2.highlighted = NO;
        [_checkButton2 setImage:[UIImage imageNamed:@"未选中"] forState:UIControlStateNormal];
    }else{
        _checkButton1.selected = NO;
        _checkButton1.highlighted = NO;
        [_checkButton1 setImage:[UIImage imageNamed:@"未选中"] forState:UIControlStateNormal];
        
    }
    
}


#pragma  mark - request
- (void)addStaff{
    
    __block typeof(self) weakSelf = self;
    if (!_mobileTextField.text || _mobileTextField.text.length == 0) {
        return;
    }
    
    if (!_nameTextField.text || _nameTextField.text.length == 0) {
        return;
    }
    
    if (!_checkButton1.selected && !_checkButton2.selected) {
        return;
    }
    
    /*
     1
     员工手机号
     mobileNbr
     2
     员工姓名
     realName
     3
     账号类型
     userLvl
     4
     所属商店
     shopCode
     */
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary *params = [[NSMutableDictionary alloc]init];
    
    [params setObject:_mobileTextField.text forKey:@"mobileNbr"];
    [params setObject:_nameTextField.text forKey:@"realName"];
    
    NSString *userLevel = @"1";
    if (_checkButton1.selected) {
        userLevel = @"2";
    }
    [params setObject:userLevel forKey:@"userLvl"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    
    NSString* vcode = [gloabFunction getSign:@"addStaff" strParams:_mobileTextField.text];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"addStaff" withParameters:params success:^(AFHTTPRequestOperation *operation, NSDictionary *responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                if (weakSelf.delegate) {
                    [weakSelf.delegate didSuccessUpdateStuff:responseObject];
                }
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            case 60000:
                CSAlert(@"请输入手机号码");
                break;
            case 60001:
                CSAlert(@"手机号码不正确");
                break;
            case 60003:
                CSAlert(@"手机号已经被使用");
                break;
            case 80040 :
                CSAlert(@"请输入员工姓名");
                break;
            case 80041 :
                CSAlert(@"请输入员工类型");
                break;
            default:
                break;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}


- (void)updateStaff{
    
    __block typeof(self) weakSelf = self;
    
    if (!_mobileTextField.text || _mobileTextField.text.length == 0) {
        return;
    }
    
    if (!_nameTextField.text || _nameTextField.text.length == 0) {
        return;
    }
    
    if (!_checkButton1.selected && !_checkButton2.selected) {
        return;
    }
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary *params = [[NSMutableDictionary alloc]init];
    
    [params setObject:_mobileTextField.text forKey:@"mobileNbr"];
    [params setObject:_nameTextField.text forKey:@"realName"];
    [params setObject:_stuffInfoDic[@"staffCode"] forKey:@"staffCode"];
    
    NSNumber *userLevel = @(1);
    if (_checkButton1.selected) {
        userLevel = @(2);
    }
    [params setObject:userLevel forKey:@"type"];
    
    NSString* vcode = [gloabFunction getSign:@"updateStaff" strParams:_mobileTextField.text];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [SVProgressHUD showWithStatus:ProgressHudStr];
    
    [self.jsonPrcClient invokeMethod:@"updateStaff" withParameters:params success:^(AFHTTPRequestOperation *operation, NSDictionary *responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                if (weakSelf.delegate) {
                    [weakSelf.delegate didSuccessUpdateStuff:responseObject];
                }
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            case 60000:
                CSAlert(@"请输入手机号码");
                break;
            case 60001:
                CSAlert(@"手机号码不正确");
                break;
            case 60003:
                CSAlert(@"手机号已经被使用");
                break;
            case 80040 :
                CSAlert(@"请输入员工姓名");
                break;
            case 80041 :
                CSAlert(@"请输入员工类型");
                break;
            default:
                break;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}


@end

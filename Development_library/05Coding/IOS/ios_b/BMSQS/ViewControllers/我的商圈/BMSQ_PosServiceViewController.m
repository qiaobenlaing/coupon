//
//  BMSQ_PosServiceViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/8/5.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_PosServiceViewController.h"
#import "SLCGRectHelper.h"
#import "AppDelegate+ScreenLayout.h"
#import "TPKeyboardAvoidingTableView.h"
#import "BMSQ_AgreementViewController.h"


@interface CheckBoxCell : UITableViewCell

@property (nonatomic ,strong) UIImageView *checkboxImageView;
@end

@implementation CheckBoxCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        _checkboxImageView  = [[UIImageView alloc] initWithFrame:CGRectZero];
        [self.contentView addSubview:_checkboxImageView];
    }
    return self;
}

- (void)layoutSubviews{
    [super layoutSubviews];
    
    SLSizeScale scale = [AppDelegate autoSizeScale];
    _checkboxImageView.frame = CGRectMake(self.frame.size.width - 50, self.frame.size.height/2 - 40/2, 40*scale.x, 40*scale.x);
    
    
}


- (void)setSelected:(BOOL)selected{
    [super setSelected:selected];
    if (selected) {
        _checkboxImageView.image = [UIImage imageNamed:@"选中"];
    }else{
        _checkboxImageView.image = [UIImage imageNamed:@"未选中"];
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    if (selected) {
        _checkboxImageView.image = [UIImage imageNamed:@"选中"];
    }else{
        _checkboxImageView.image = [UIImage imageNamed:@"未选中"];
    }
    
}



@end



@interface BMSQ_PosServiceViewController ()<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic ,strong) TPKeyboardAvoidingTableView* tableView;
@property (nonatomic ,strong) UITextView *textView;
@property (nonatomic ,strong) UIWebView *webView;

@end

@implementation BMSQ_PosServiceViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
   // [self setNavTitle:@"POS服务"];
   // [self setNavBackItem];
    
    self.textView = [[UITextView alloc] initWithFrame:CGRectZero];
    _textView.layer.borderColor = [UIColor lightGrayColor].CGColor;
    _textView.layer.borderWidth = 1.0;
    
    
//    UIButton *_rightButton = [UIButton buttonWithType:UIButtonTypeCustom];
//    _rightButton.frame = CGRectMake(self.navigationView.frame.size.width -50, 20, 40, 44);
//    [_rightButton setTitle:@"提交" forState:UIControlStateNormal];
//    _rightButton.titleLabel.font = [UIFont systemFontOfSize:15];
//    _rightButton.titleLabel.textAlignment = NSTextAlignmentRight;
//    [_rightButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//    [_rightButton addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
//    [self setNavRightBarItem:_rightButton];
    [self.navigationItem setTitle:@"POS服务"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self customRightBtn];
    
    
    _tableView = [[TPKeyboardAvoidingTableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT +44)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"UITableViewCell"];
    [_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"textviewcell"];
    
    UIView *footerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, _tableView.frame.size.width, 44)];
    _tableView.tableFooterView = footerView;
    
    [self.view addSubview:_tableView];
    
}

- (void)customRightBtn
{
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"提交" style:UIBarButtonItemStylePlain target:self action:@selector(didClickRightButton:) ];
    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = item;
    
}

- (void)didClickRightButton:(id)sender {
    
    NSIndexPath *index = _tableView.indexPathForSelectedRow;
    
    if (index) {
        if (index.row == 0) {
            [self applyPosServerWithType:@(2)];
        }else if (index.row == 1){
            [self applyPosServerWithType:@(3)];
        }else{
            [self applyPosServerWithType:@(4)];
        }
        [self.navigationController popViewControllerAnimated:YES];
    }
    
}



#pragma mark - Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 2;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if (section == 0) {
        return 3;
    }
    return 4;
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    
    if (section == 0) {
        return @"服务类型";
    }
    return @"";
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    
    NSString *title =  @"    服务类型";
    if (section == 1) {
        title = @"    服务说明";
    }
    
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 60, 40)];
    titleLabel.text = title;
    titleLabel.font = [UIFont systemFontOfSize:14.0];
    titleLabel.textColor = [UIColor lightGrayColor];
    
    return titleLabel;
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 40;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 1 && indexPath.row == 0) {
        return 230;
    }
    return 44;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {
        static NSString *checkboxCell = @"checkboxCell";
        CheckBoxCell *cell = [tableView dequeueReusableCellWithIdentifier:checkboxCell];
        if (!cell) {
            cell = [[CheckBoxCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:checkboxCell];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if (indexPath.row ==0 ) {
            cell.imageView.image = [UIImage imageNamed:@"耗材配送"];
            cell.textLabel.text = @"耗材配送";
        }
        if (indexPath.row ==1 ) {
            cell.imageView.image = [UIImage imageNamed:@"故障报修"];
            cell.textLabel.text = @"故障报修";
        }
        if (indexPath.row ==2 ) {
            cell.imageView.image = [UIImage imageNamed:@"其他"];
            cell.textLabel.text = @"其他";
        }
        
        return cell;
        
    }else {
        
        if (indexPath.row == 0){
            
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"textviewcell"];
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"textviewcell"];
            }
            cell.backgroundColor = APP_VIEW_BACKCOLOR;
            _textView.frame = CGRectMake(10, 0, self.view.frame.size.width -2*10, 230-10);
            [cell.contentView addSubview:_textView];
            return cell;
            
        }else{
            UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"UITableViewCell"];
            if (!cell) {
                cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"UITableViewCell"];
 
            }

            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            
            if (indexPath.row == 1 ) {
                cell.imageView.image = [UIImage imageNamed:@"申请pos机"];
                cell.textLabel.text = @"申请工行POS机刷银行卡";
            }
            if (indexPath.row == 2 ) {
                cell.imageView.image = [UIImage imageNamed:@"常见问题"];
                cell.textLabel.text = @"常见问题";
            }
            if (indexPath.row == 3 ) {
                cell.accessoryType = UITableViewCellAccessoryNone;
                cell.imageView.image = [UIImage imageNamed:@"客户热线服务"];
                cell.textLabel.text = @"商户热线服务：400-04-95588";
            }
            
            return cell;
            
        }
        
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if (indexPath.section == 1 ) {
        [tableView deselectRowAtIndexPath:indexPath animated:YES];
        
        if (indexPath.row == 1) {
            [self applyPosServerWithType:@(1)];
        }else if (indexPath.row ==2){
            
            UIStoryboard *stryBoard=[UIStoryboard storyboardWithName:@"BMSQ_Login" bundle:nil];
            BMSQ_AgreementViewController *vc = [stryBoard instantiateViewControllerWithIdentifier:@"BMSQ_Agreement"];
            vc.hidesBottomBarWhenPushed = YES;
            vc.navViewTitle = @"常见问题";
            vc.url = @"http://baomi.suanzi.cn/Api/Browser/posFaq";
            [self.tabBarController.tabBar setHidden:YES];
            [self.navigationController pushViewController:vc animated:YES];

        }else if (indexPath.row == 3){
            
            if (!_webView) {
                _webView = [[UIWebView alloc] initWithFrame:CGRectMake(0, 0, 1, 1)];
                [self.view addSubview:_webView];
            }
            [_webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:@"tel:4000495588"]]];
        }
    }
}

#pragma mark - Request


/*
 * type  1-	申请POS机；
         2-	耗材配送；
         3-	故障报修；
         4- 其他；
 */
- (void)applyPosServerWithType:(NSNumber *)type{
    
    /*
     1
     商家编码
     shopCode
     2
     服务类型
     type
     */

    if (!type ) {
        return;
    }
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary *params = [[NSMutableDictionary alloc]init];
    
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:type forKey:@"type"];
    [params setObject:_textView.text forKey:@"remark"];
    
    NSString* vcode = [gloabFunction getSign:@"applyPosServer" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"applyPosServer" withParameters:params success:^(AFHTTPRequestOperation *operation, NSDictionary *responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                CSAlert(@"申请成功");
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            case 50314:
                CSAlert(@"商家编码不正确");
                break;
            case 50316:
                CSAlert(@"商家不存在");
                break;
            case 50800:
                CSAlert(@"服务类型不正确");
                break;
            default:
                break;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}



@end

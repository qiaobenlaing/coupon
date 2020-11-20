//
//  BMSQ_StaffListTableViewController.m
//  BMSQS
//
//  Created by Sencho Kong on 15/7/31.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_StaffListTableViewController.h"
#import "SLCGRectHelper.h"
#import "BMSQ_StaffAddViewController.h"

@interface StaffCell : UITableViewCell

@end

@implementation StaffCell

- (void)layoutSubviews{
    [super layoutSubviews];
    
    self.imageView.frame = SLRectMake(15, self.frame.size.height/2 - 20/2, 16, 20);
    
    CGRect rect = self.textLabel.frame;
    rect.origin = CGPointMake(CGRectGetMaxX(self.imageView.frame) +10, 10);
    rect.size   = CGSizeMake(self.frame.size.width - rect.origin.x - 44, 20);
    self.textLabel.frame = rect;
    self.textLabel.font = [UIFont systemFontOfSize:15.0];
    
    rect = self.detailTextLabel.frame;
    rect.origin = CGPointMake(CGRectGetMaxX(self.imageView.frame) +10, CGRectGetMaxY(self.textLabel.frame) +5);
    rect.size.height = self.frame.size.height - rect.origin.y-10;
    rect.size.width  = CGRectGetWidth(self.textLabel.frame);
    self.detailTextLabel.frame = rect;
    self.detailTextLabel.numberOfLines = 0;
    self.detailTextLabel.font = [UIFont systemFontOfSize:15.0];
    self.detailTextLabel.textColor = [UIColor blackColor];
    self.tintColor = [UIColor colorWithRed:160.0/255.0 green:0 blue:1.0/255.0 alpha:1.0];
    
    
}

@end


@interface BMSQ_StaffListTableViewController ()<UITableViewDataSource,UITableViewDelegate,UIAlertViewDelegate,BMSQ_StaffAddViewControllerDelegate>{
    
    
}

@property (nonatomic ,strong) UITableView* tableView;
@property (nonatomic ,strong) UIButton* bottomButton;
@property (nonatomic ,strong) UIButton *rightButton;
@property (nonatomic ,strong) NSArray *stuffList;
@property (nonatomic ,assign) BOOL displayTwoSection; // 显示一个section还是2个

@end

@implementation BMSQ_StaffListTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
//    [self setNavTitle:@"员工管理"];
//    [self setNavBackItem];
//    
//    _rightButton = [UIButton buttonWithType:UIButtonTypeCustom];
//    _rightButton.frame = CGRectMake(self.view.frame.size.width -60, 20, 60, 44);
//    [_rightButton setTitle:@"删除" forState:UIControlStateNormal];
//    _rightButton.titleLabel.font = [UIFont systemFontOfSize:15];
//    _rightButton.titleLabel.textAlignment = NSTextAlignmentRight;
//    [_rightButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
//    [_rightButton addTarget:self action:@selector(didClickRightButton:) forControlEvents:UIControlEventTouchUpInside];
//    [self setNavRightBarItem:_rightButton];

    [self.navigationItem setTitle:@"员工管理"];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    [self customRightBtn];
    
    _tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, APP_VIEW_CAN_USE_HEIGHT  - 44)];
    _tableView.dataSource = self;
    _tableView.delegate = self;
    _tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    _tableView.allowsMultipleSelectionDuringEditing = YES;
    [_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"UITableViewCell"];
    
    UIView *footerView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, _tableView.frame.size.width, 44)];
    _tableView.tableFooterView = footerView;
    
    [self.view addSubview:_tableView];
    
    
    self.bottomButton = [UIButton buttonWithType:UIButtonTypeCustom];
    _bottomButton.backgroundColor = [UIColor whiteColor];
    _bottomButton.frame = CGRectMake(0, self.view.frame.size.height - 44, _tableView.frame.size.width, 44);
    [_bottomButton setTitle:@"添加员工" forState:UIControlStateNormal];
    [_bottomButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
    [_bottomButton addTarget:self action:@selector(didClickBottomButton:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_bottomButton];
    
    
    [self getListStaff];
    
}

- (void)customRightBtn
{
    UIBarButtonItem *item = [[UIBarButtonItem alloc] initWithTitle:@"删除" style:UIBarButtonItemStylePlain target:self action:@selector(didClickRightButton:) ];
    [item setTitleTextAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:16.f],NSForegroundColorAttributeName: [UIColor whiteColor],} forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = item;
    
}

- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    [self setHidesBottomBarWhenPushed:YES];
}


- (void)didClickRightButton:(UIButton *)sender {

    _tableView.editing = !_tableView.editing;
    [_tableView reloadData];
    
    if (_tableView.editing) {
        [sender setTitle:@"取消" forState:UIControlStateNormal];
        [_bottomButton setBackgroundColor:[UIColor colorWithRed:160.0/255.0 green:0 blue:1.0/255.0 alpha:1.0]];
        [_bottomButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [_bottomButton setTitle:@"删除" forState:UIControlStateNormal];
    }else{
        [sender setTitle:@"删除" forState:UIControlStateNormal];
        [_bottomButton setBackgroundColor:[UIColor whiteColor]];
        [_bottomButton setTitleColor:[UIColor grayColor] forState:UIControlStateNormal];
        [_bottomButton setTitle:@"添加员工" forState:UIControlStateNormal];
    }
}


- (void)didClickBottomButton:(UIButton *)sender{
    
    if (_tableView.editing) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"提示" message:@"确认删除员工" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确认", nil];
        [alert show];
    }else{
        BMSQ_StaffAddViewController *pushVC = [[BMSQ_StaffAddViewController alloc] initWithNibName:@"BMSQ_StaffAddViewController" bundle:nil];
        pushVC.delegate = self;
        [self.navigationController pushViewController:pushVC animated:YES];
    }
    
}


#pragma  mark - UIAlertView Delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if (buttonIndex == 1) {
        
        NSDictionary *stuff =  _stuffList[_tableView.indexPathForSelectedRow.section][_tableView.indexPathForSelectedRow.row];
        [self delStaffCode:stuff[@"staffCode"]];
    }
    
}


#pragma  mark - request
- (void)getListStaff{
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    NSString* vcode = [gloabFunction getSign:@"listStaff" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"listStaff" withParameters:params success:^(AFHTTPRequestOperation *operation, NSDictionary *responseObject) {
        [SVProgressHUD dismiss];
        NSMutableArray *level1StuffArray = [NSMutableArray array];
        NSMutableArray *level2StuffArray = [NSMutableArray array];
        
        if (responseObject) {
            for (NSDictionary *stuff in responseObject[@"staffList"]) {
                if ([stuff[@"userLvl"] integerValue] == 2) {
                    [level2StuffArray addObject:stuff];
                }else{
                    [level1StuffArray addObject:stuff];
                }
            }
        }
        weakSelf.stuffList = @[level2StuffArray,level1StuffArray];
        weakSelf.displayTwoSection = level1StuffArray.count>0 && level2StuffArray.count > 0?YES:NO;

        [weakSelf.tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
}

- (void)delStaffCode:(NSString *)staffDode{
    
    /*
     员工编码
     staffCode
     */
    
    if (!staffDode) {
        return;
    }
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary *params = [[NSMutableDictionary alloc]init];
    
    [params setObject:staffDode forKey:@"staffCode"];
    
    NSString* vcode = [gloabFunction getSign:@"delStaff" strParams:staffDode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __block typeof(self) weakSelf = self;
    [SVProgressHUD showWithStatus:ProgressHudStr];
    [self.jsonPrcClient invokeMethod:@"delStaff" withParameters:params success:^(AFHTTPRequestOperation *operation, NSDictionary *responseObject) {
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
        switch (resCode) {
            case 50000:{
                
                [weakSelf getListStaff];
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            default:
                break;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
    }];
    
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    if (_displayTwoSection) {
        return 2;
    }
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    if (_displayTwoSection) {
        
        return [_stuffList[section] count];
    }else{
        if ([_stuffList[0] count] > 0) {
            return [_stuffList[0] count];
        }else if ([_stuffList[1] count] > 0){
            return [_stuffList[1] count];
        }else{
            return 0;
        }
    }
    
}


- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    
    if (_displayTwoSection) {
        if (section == 0) {
            return @"老板店长类";
        }else{
            return @"员工类";
        }
    }else{
        return @"员工类";
    }
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 65;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *identifier = @"StaffCell";
    StaffCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    if (!cell) {
        cell = [[StaffCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:identifier];
    }
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    NSDictionary *stuffDic;
    
    if (_displayTwoSection) {
        stuffDic = _stuffList[indexPath.section][indexPath.row];
    }else{
        if ([_stuffList[0] count] > 0) {
            stuffDic = _stuffList[0][indexPath.row];
        }else if ([_stuffList[1] count] > 0){
            stuffDic = _stuffList[1][indexPath.row];
        }
    }
    
    cell.imageView.image = [UIImage imageNamed:@"员工"];
    cell.textLabel.text = [NSString stringWithFormat:@"帐号：%@",stuffDic[@"mobileNbr"]];
    cell.detailTextLabel.text = [NSString stringWithFormat:@"员工姓名：%@",stuffDic[@"realName"]];
    
    
    return cell;
}


- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    if (tableView.editing) {
        return;
    }else{
        [tableView deselectRowAtIndexPath:indexPath animated:YES];

        NSDictionary *stuffDic;
        if (_displayTwoSection) {
            stuffDic = _stuffList[indexPath.section][indexPath.row];
        }else{
            if ([_stuffList[0] count] > 0) {
                stuffDic = _stuffList[0][indexPath.row];
            }else if ([_stuffList[1] count] > 0){
                stuffDic = _stuffList[1][indexPath.row];
            }
        }
        
        BMSQ_StaffAddViewController *pushVC = [[BMSQ_StaffAddViewController alloc] initWithNibName:@"BMSQ_StaffAddViewController" bundle:nil];
        pushVC.delegate = self;
        pushVC.stuffInfoDic = stuffDic;
        [self.navigationController pushViewController:pushVC animated:YES];
    }
    
}

- (void)didSuccessUpdateStuff:(NSDictionary *)stuffInfo{
    
    [self getListStaff];
}

@end

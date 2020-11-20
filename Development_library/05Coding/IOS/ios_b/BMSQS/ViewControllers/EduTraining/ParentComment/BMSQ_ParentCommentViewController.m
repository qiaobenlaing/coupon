//
//  BMSQ_ParentCommentViewController.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/10.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_ParentCommentViewController.h"
#import "SVProgressHUD.h"
#import "MJRefresh.h"
#import "ParentCommentViewCell.h"
@interface BMSQ_ParentCommentViewController ()<UITableViewDataSource, UITableViewDelegate, ParentCommentViewCellDelegate>

{
    int page;
    NSString * remarkCodeStr;
    NSString * shopRemarkStr;
    
}
@property (nonatomic, strong) UITableView * tableView;
@property (nonatomic, strong) NSMutableArray * dataSorceAry;

@end

@implementation BMSQ_ParentCommentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = APP_VIEW_BACKCOLOR;
    [self setNavTitle:@"家长点评"];
    [self setNavBackItem];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillHideNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onKeyboardNotification:) name:UIKeyboardWillShowNotification object:nil];
    
    
    self.dataSorceAry = [@[] mutableCopy];
    page = 1;
    [self setViewUp];
    [self getClassRemarkList];
    
}

- (void)setViewUp
{
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - 65) style:UITableViewStyleGrouped];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.keyboardDismissMode  = UIScrollViewKeyboardDismissModeInteractive;
    [self.tableView addHeaderWithTarget:self action:@selector(headerRereshing)];
    
    [self.tableView addFooterWithTarget:self action:@selector(footerRereshing)];
    
    
    [self.view addSubview:self.tableView];
}


#pragma mark -------  UITableViewDataSource, UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.dataSorceAry.count == 0) {
        return 0;
    }else{
        return self.dataSorceAry.count;
    }
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString * cell_id = @"ParentCommentViewCellss";
    ParentCommentViewCell * cell = [tableView dequeueReusableCellWithIdentifier:cell_id];
    if (!cell) {
        cell = [[ParentCommentViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cell_id];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.parentDelegate = self;
    if (self.dataSorceAry.count > 0) {
        [cell setCellParentCommentDic:self.dataSorceAry[indexPath.row]];
    }
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 1;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString * str = @"";
    if (self.dataSorceAry.count != 0) {
        str = self.dataSorceAry[indexPath.row][@"remark"];
    }
    CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH -  100, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:12.f]} context:nil].size;
    NSMutableArray * array = [[NSMutableArray alloc] init];
    array = self.dataSorceAry[indexPath.row][@"classRemarkImg"];
    if (size.height > 20) {
        if (array.count == 0) {
            return size.height + 135;
        }else{
            return size.height + 235;
        }
        
    }else{
        if (array.count == 0) {
            return 155;
        }else{
            return 255;
        }
        
    }
    
}


#pragma mark ------ getClassRemarkList  获取全部课程的评论列表
- (void)getClassRemarkList
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:@"" forKey:@"classCode"];
    [params setObject:[NSString stringWithFormat:@"%d", page] forKey:@"page"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"getClassRemarkList" strParams:@""];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getClassRemarkList" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        NSLog(@"%@", responseObject);
        
        [self.tableView headerEndRefreshing];
        [self.tableView footerEndRefreshing];
        
        if(page == 1) {
            [self.dataSorceAry removeAllObjects];
        }
        
        if ([responseObject isKindOfClass:[NSDictionary class]]) {
            [self.dataSorceAry addObjectsFromArray:responseObject[@"classRemarkList"]];
        }

        if (self.dataSorceAry.count == 0) {
            CSAlert(@"暂无点评信息");
        }
 
        [self.tableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        [self.tableView headerEndRefreshing];
        [self.tableView footerEndRefreshing];
        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}



#pragma mark ---- 刷新加载
- (void)headerRereshing{
    page = 1;
    [self getClassRemarkList];
}
- (void)footerRereshing{
    page ++;
    [self getClassRemarkList];
}


#pragma mark ------- ParentCommentViewCellDelegate
- (void)setParentWithRemarkCode:(NSString *)remarkCode shopRemark:(NSString *)shopRemark
{
    remarkCodeStr = remarkCode;
    shopRemarkStr = shopRemark;
    [self replyClassRemark];
}

#pragma mark ----- replyClassRemark  商家回复评论
- (void)replyClassRemark
{
    [SVProgressHUD showWithStatus:@""];
    
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    [params setObject:remarkCodeStr forKey:@"remarkCode"];
    [params setObject:shopRemarkStr forKey:@"shopRemark"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    NSString* vcode = [gloabFunction getSign:@"replyClassRemark" strParams:remarkCodeStr];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"replyClassRemark" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
    
        NSLog(@"%@", responseObject);
        NSString * code = [NSString stringWithFormat:@"%@", responseObject[@"code"]];
        if (code.intValue == 50000) {
            CSAlert(@"成功");
            page = 1;
            [self getClassRemarkList];
        }else if (code.intValue == 20000){
            CSAlert(@"失败");
        }else if (code.intValue == 77032){
            CSAlert(@"商家回复内容为空");
        }
        

        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        

        [SVProgressHUD dismiss];
        
        CSAlert(@"数据请求失败");
        
        
    }];

}


#pragma mark - Keyboard notification

- (void)onKeyboardNotification:(NSNotification *)notification {
    //Reset constraint constant by keyboard height
    if ([notification.name isEqualToString:UIKeyboardWillShowNotification]) {
        CGRect keyboardFrame = ((NSValue *) notification.userInfo[UIKeyboardFrameEndUserInfoKey]).CGRectValue;
        
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y-keyboardFrame.size.height);
        
    } else if ([notification.name isEqualToString:UIKeyboardWillHideNotification]) {
        _tableView.frame = CGRectMake(_tableView.frame.origin.x, _tableView.frame.origin.y, _tableView.frame.size.width, APP_VIEW_HEIGHT-APP_VIEW_ORIGIN_Y);
    }
    
    //Animate change
    [UIView animateWithDuration:0.8f animations:^{
        [self.view layoutIfNeeded];
    }];
    
}

- (void)dealloc {
   
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification
                                                  object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification
                                                  object:nil];
    
}



#pragma mark ------ 内存管理
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
